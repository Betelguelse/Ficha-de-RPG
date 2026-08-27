package br.com.ficha.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import br.com.ficha.model.Anotacao;
import br.com.ficha.model.Atributos;
import br.com.ficha.model.Equipamento;
import br.com.ficha.model.Ficha;
import br.com.ficha.model.Habilidade;
import br.com.ficha.model.Item;
import br.com.ficha.service.AnotacaoService;
import br.com.ficha.service.EquipamentoService;
import br.com.ficha.service.FichaService;
import br.com.ficha.service.HabilidadeService;
import br.com.ficha.service.ItemService;

public class FichaTui {
    private static final String[] OPCOES = {
        "Dados do personagem", "Atributos", "Itens", "Inventário",
        "Equipamentos", "Habilidades", "Anotações", "Sair"
    };
    private static final String[] CAMPOS_DADOS = {
        "Nome", "Idade", "Sexo", "Vida máxima", "Vida temporária", "Vida atual",
        "Status", "Raça", "Classe", "Nível", "Moedas de bronze", "Moedas de prata", "Moedas de ouro"
    };
    private static final String[] CAMPOS_ATRIBUTOS = {
        "Bônus de Proficiência", "Inteligência", "Sabedoria", "Força",
        "Destreza", "Constituição", "Carisma"
    };
    private static final String[] TIPOS_ITEM = {
        "Arma", "Defesa", "Utilizável", "Ferramenta", "Material", "Acessório", "Outro"
    };
    private static final String[] TIPOS_EQUIPAMENTO = {"Arma", "Armadura", "Outro"};
    private static final String[] CATEGORIAS_ARMADURA = {"Leve", "Média", "Pesada"};
    private static final String[] RARIDADES = {"Comum", "Incomum", "Raro", "Muito Raro", "Lendário"};
    private static final String[] CAMPOS_ITEM = {"Nome", "Quantidade", "Tipo", "Descrição"};
    private static final String[] CAMPOS_EQUIPAMENTO = {
        "Nome", "Tipo", "Categoria", "Dano", "Tipo de dano", "Peso", "Valor", "Raridade"
    };
    private static final String[] CAMPOS_HABILIDADE = {
        "Nome", "Nível", "Tipo", "Alcance", "Recarga", "Custo", "Descrição"
    };
    private static final String[] CAMPOS_ANOTACAO = {"Título", "Texto"};

    private final FichaService fichaService;
    private final ItemService itemService;
    private final EquipamentoService equipamentoService;
    private final HabilidadeService habilidadeService;
    private final AnotacaoService anotacaoService;

    private int opcaoSelecionada;
    private int selecao;
    private int indiceRegistroEdicao;
    private boolean adicionandoRegistro;
    private String[] rascunho;
    private boolean[] permissoesRascunho;
    private Tela tela = Tela.PRINCIPAL;
    private Tela retornoDetalheItem = Tela.ITENS;
    private boolean editandoInline;
    private StringBuilder entradaInline = new StringBuilder();
    private int cursorInline;
    private boolean selecionarTudoInline;
    private String[] opcoesInline;
    private int opcaoInline;
    private AcaoEdicao acaoEdicao;
    private boolean confirmando;
    private Runnable acaoConfirmacao;
    private String mensagemConfirmacao;
    private boolean escolhendoOpcaoRapida;
    private String descricaoOpcaoRapida;
    private String[] opcoesRapidas;
    private int opcaoRapida;
    private AcaoEdicao acaoOpcaoRapida;
    private boolean popupErroAberto;
    private String mensagemPopupErro;
    private boolean popupSaidaAberto;
    private int opcaoPopupSaida = 1;
    private String aviso = "";
    private long avisoExpiraEm;
    private int cursorTelaX;
    private int cursorTelaY;

    public FichaTui(
        FichaService fichaService,
        ItemService itemService,
        EquipamentoService equipamentoService,
        HabilidadeService habilidadeService,
        AnotacaoService anotacaoService
    ) {
        this.fichaService = fichaService;
        this.itemService = itemService;
        this.equipamentoService = equipamentoService;
        this.habilidadeService = habilidadeService;
        this.anotacaoService = anotacaoService;
    }

    public void iniciar() throws IOException {
        try (Screen screen = new TerminalScreen(new DefaultTerminalFactory().createTerminal())) {
            screen.startScreen();
            screen.setCursorPosition(null);

            boolean executando = true;
            while (executando) {
                desenhar(screen);
                executando = processarTecla(aguardarEntrada(screen));
            }
        }
    }

    private KeyStroke aguardarEntrada(Screen screen) throws IOException {
        while (true) {
            KeyStroke tecla = screen.pollInput();
            if (tecla != null) return tecla;
            if (expirarAviso()) desenhar(screen);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new KeyStroke(KeyType.EOF);
            }
        }
    }

    private boolean processarTecla(KeyStroke tecla) {
        if (tecla.getKeyType() == KeyType.EOF) return false;
        if (popupSaidaAberto) return processarPopupSaida(tecla);
        if (popupErroAberto) return processarPopupErro(tecla);
        if (editandoInline) return processarEdicaoInline(tecla);
        if (confirmando) return processarConfirmacao(tecla);
        if (escolhendoOpcaoRapida) return processarOpcaoRapida(tecla);
        if (caractere(tecla, 'q')) {
            abrirPopupSaida();
            return true;
        }

        return switch (tela) {
            case PRINCIPAL -> processarPrincipal(tecla);
            case DADOS -> processarDados(tecla);
            case ATRIBUTOS -> processarAtributos(tecla);
            case ITENS -> processarItens(tecla);
            case EDICAO_ITEM -> processarEditorItem(tecla);
            case INVENTARIO -> processarInventario(tecla);
            case DETALHE_ITEM -> processarDetalhe(tecla, Tela.ITENS);
            case EQUIPAMENTOS -> processarEquipamentos(tecla);
            case EDICAO_EQUIPAMENTO -> processarEditorEquipamento(tecla);
            case PERMISSOES_EQUIPAMENTO -> processarPermissoesEquipamento(tecla);
            case DETALHE_EQUIPAMENTO -> processarDetalhe(tecla, Tela.EQUIPAMENTOS);
            case HABILIDADES -> processarHabilidades(tecla);
            case EDICAO_HABILIDADE -> processarEditorHabilidade(tecla);
            case DETALHE_HABILIDADE -> processarDetalhe(tecla, Tela.HABILIDADES);
            case ANOTACOES -> processarAnotacoes(tecla);
            case EDICAO_ANOTACAO -> processarEditorAnotacao(tecla);
            case DETALHE_ANOTACAO -> processarDetalhe(tecla, Tela.ANOTACOES);
        };
    }

    private boolean processarPrincipal(KeyStroke tecla) {
        switch (tecla.getKeyType()) {
            case ArrowUp -> opcaoSelecionada = circular(opcaoSelecionada - 1, OPCOES.length);
            case ArrowDown -> opcaoSelecionada = circular(opcaoSelecionada + 1, OPCOES.length);
            case Enter -> {
                if (opcaoSelecionada == OPCOES.length - 1) {
                    abrirPopupSaida();
                    return true;
                }
                tela = switch (opcaoSelecionada) {
                    case 0 -> Tela.DADOS;
                    case 1 -> Tela.ATRIBUTOS;
                    case 2 -> Tela.ITENS;
                    case 3 -> Tela.INVENTARIO;
                    case 4 -> Tela.EQUIPAMENTOS;
                    case 5 -> Tela.HABILIDADES;
                    case 6 -> Tela.ANOTACOES;
                    default -> Tela.PRINCIPAL;
                };
                selecao = 0;
            }
            case Escape -> abrirPopupSaida();
            default -> { }
        }
        return true;
    }

    private boolean processarDados(KeyStroke tecla) {
        if (navegar(tecla, CAMPOS_DADOS.length)) return true;
        if (tecla.getKeyType() == KeyType.Enter) editarDado();
        else if (tecla.getKeyType() == KeyType.Escape) voltarAoPrincipal();
        return true;
    }

    private boolean processarAtributos(KeyStroke tecla) {
        if (navegar(tecla, CAMPOS_ATRIBUTOS.length)) return true;
        if (tecla.getKeyType() == KeyType.Enter) editarAtributo();
        else if (tecla.getKeyType() == KeyType.Escape) voltarAoPrincipal();
        return true;
    }

    private boolean processarItens(KeyStroke tecla) {
        List<Item> itens = itemService.listarItens();
        if (navegar(tecla, itens.size())) return true;
        if (tecla.getKeyType() == KeyType.Enter && !itens.isEmpty()) {
            retornoDetalheItem = Tela.ITENS;
            tela = Tela.DETALHE_ITEM;
        }
        else if (tecla.getKeyType() == KeyType.Escape) voltarAoPrincipal();
        else if (caractere(tecla, 'a')) abrirAdicao(
            Tela.EDICAO_ITEM,
            new String[] {"", "1", TIPOS_ITEM[0], ""}
        );
        else if (caractere(tecla, 'e') && !itens.isEmpty()) abrirEditor(Tela.EDICAO_ITEM);
        else if (tecla.getKeyType() == KeyType.Delete && !itens.isEmpty()) excluirItem();
        return true;
    }

    private boolean processarInventario(KeyStroke tecla) {
        List<Item> itens = itemService.listarItens();
        if (navegar(tecla, itens.size())) return true;
        if (tecla.getKeyType() == KeyType.Enter && !itens.isEmpty()) {
            retornoDetalheItem = Tela.INVENTARIO;
            tela = Tela.DETALHE_ITEM;
        }
        else if (tecla.getKeyType() == KeyType.Escape) voltarAoPrincipal();
        return true;
    }

    private boolean processarEquipamentos(KeyStroke tecla) {
        List<Equipamento> equipamentos = equipamentoService.listar();
        if (navegar(tecla, equipamentos.size())) return true;
        if (tecla.getKeyType() == KeyType.Enter && !equipamentos.isEmpty()) tela = Tela.DETALHE_EQUIPAMENTO;
        else if (tecla.getKeyType() == KeyType.Escape) voltarAoPrincipal();
        else if (caractere(tecla, 'a')) abrirAdicao(
            Tela.EDICAO_EQUIPAMENTO,
            new String[] {"", TIPOS_EQUIPAMENTO[0], "", "N/A", "N/A", "0", "1", RARIDADES[0]}
        );
        else if (caractere(tecla, 'e') && !equipamentos.isEmpty()) abrirEditor(Tela.EDICAO_EQUIPAMENTO);
        else if (caractere(tecla, 'x') && !equipamentos.isEmpty()) alternarEquipamento();
        else if (caractere(tecla, 'p')) abrirPermissoesEquipamento();
        return true;
    }

    private boolean processarHabilidades(KeyStroke tecla) {
        List<Habilidade> habilidades = habilidadeService.listarHabilidades();
        if (navegar(tecla, habilidades.size())) return true;
        if (tecla.getKeyType() == KeyType.Enter && !habilidades.isEmpty()) tela = Tela.DETALHE_HABILIDADE;
        else if (tecla.getKeyType() == KeyType.Escape) voltarAoPrincipal();
        else if (caractere(tecla, 'a')) abrirAdicao(
            Tela.EDICAO_HABILIDADE,
            new String[] {"", "0", "", "", "", "", ""}
        );
        else if (caractere(tecla, 'e') && !habilidades.isEmpty()) abrirEditor(Tela.EDICAO_HABILIDADE);
        else if (tecla.getKeyType() == KeyType.Delete && !habilidades.isEmpty()) excluirHabilidade();
        return true;
    }

    private boolean processarAnotacoes(KeyStroke tecla) {
        List<Anotacao> anotacoes = anotacaoService.listar();
        if (navegar(tecla, anotacoes.size())) return true;
        if (tecla.getKeyType() == KeyType.Enter && !anotacoes.isEmpty()) tela = Tela.DETALHE_ANOTACAO;
        else if (tecla.getKeyType() == KeyType.Escape) voltarAoPrincipal();
        else if (caractere(tecla, 'a')) abrirAdicao(Tela.EDICAO_ANOTACAO, new String[] {"", ""});
        else if (caractere(tecla, 'e') && !anotacoes.isEmpty()) abrirEditor(Tela.EDICAO_ANOTACAO);
        else if (tecla.getKeyType() == KeyType.Delete && !anotacoes.isEmpty()) excluirAnotacao();
        return true;
    }

    private boolean processarDetalhe(KeyStroke tecla, Tela destino) {
        if (tecla.getKeyType() == KeyType.Escape) {
            tela = destino == Tela.ITENS ? retornoDetalheItem : destino;
        }
        return true;
    }

    private boolean processarEditorItem(KeyStroke tecla) {
        if (navegar(tecla, CAMPOS_ITEM.length)) return true;
        if (tecla.getKeyType() == KeyType.Enter) iniciarEdicaoCampoItem();
        else if (caractere(tecla, 's') && adicionandoRegistro) salvarNovoItem();
        else if (tecla.getKeyType() == KeyType.Escape) voltarParaLista(Tela.ITENS);
        return true;
    }

    private boolean processarEditorEquipamento(KeyStroke tecla) {
        if (navegar(tecla, CAMPOS_EQUIPAMENTO.length)) return true;
        if (tecla.getKeyType() == KeyType.Enter) iniciarEdicaoCampoEquipamento();
        else if (caractere(tecla, 's') && adicionandoRegistro) salvarNovoEquipamento();
        else if (tecla.getKeyType() == KeyType.Escape) voltarParaLista(Tela.EQUIPAMENTOS);
        return true;
    }

    private boolean processarPermissoesEquipamento(KeyStroke tecla) {
        if (navegar(tecla, CATEGORIAS_ARMADURA.length)) return true;
        if (tecla.getKeyType() == KeyType.Enter
            || tecla.getKeyType() == KeyType.ArrowLeft
            || tecla.getKeyType() == KeyType.ArrowRight) {
            permissoesRascunho[selecao] = !permissoesRascunho[selecao];
        } else if (caractere(tecla, 's')) {
            try {
                equipamentoService.salvarPermissoes(permissoesRascunho);
                tela = Tela.EQUIPAMENTOS;
                selecao = indiceRegistroEdicao;
                permissoesRascunho = null;
                mostrarAvisoTemporario("Permissões atualizadas com sucesso.");
            } catch (IllegalArgumentException e) {
                mostrarErroPopup(e.getMessage());
            }
        } else if (tecla.getKeyType() == KeyType.Escape) {
            tela = Tela.EQUIPAMENTOS;
            selecao = indiceRegistroEdicao;
            permissoesRascunho = null;
            aviso = "";
            avisoExpiraEm = 0;
        }
        return true;
    }

    private boolean processarEditorHabilidade(KeyStroke tecla) {
        if (navegar(tecla, CAMPOS_HABILIDADE.length)) return true;
        if (tecla.getKeyType() == KeyType.Enter) iniciarEdicaoCampoHabilidade();
        else if (caractere(tecla, 's') && adicionandoRegistro) salvarNovaHabilidade();
        else if (tecla.getKeyType() == KeyType.Escape) voltarParaLista(Tela.HABILIDADES);
        return true;
    }

    private boolean processarEditorAnotacao(KeyStroke tecla) {
        if (navegar(tecla, CAMPOS_ANOTACAO.length)) return true;
        if (tecla.getKeyType() == KeyType.Enter) iniciarEdicaoCampoAnotacao();
        else if (caractere(tecla, 's') && adicionandoRegistro) salvarNovaAnotacao();
        else if (tecla.getKeyType() == KeyType.Escape) voltarParaLista(Tela.ANOTACOES);
        return true;
    }

    private boolean processarEdicaoInline(KeyStroke tecla) {
        if (tecla.getKeyType() == KeyType.Escape) {
            cancelarEdicaoInline();
            mostrarAvisoTemporario("Edição cancelada.");
            return true;
        }
        if (opcoesInline != null) {
            if (tecla.getKeyType() == KeyType.ArrowLeft) opcaoInline = circular(opcaoInline - 1, opcoesInline.length);
            else if (tecla.getKeyType() == KeyType.ArrowRight) opcaoInline = circular(opcaoInline + 1, opcoesInline.length);
            else if (tecla.getKeyType() == KeyType.Enter) concluirEdicaoInline(opcoesInline[opcaoInline]);
            return true;
        }

        switch (tecla.getKeyType()) {
            case Character -> {
                if (selecionarTudoInline) {
                    entradaInline.setLength(0);
                    cursorInline = 0;
                    selecionarTudoInline = false;
                }
                entradaInline.insert(cursorInline, tecla.getCharacter());
                cursorInline++;
            }
            case Backspace -> {
                if (selecionarTudoInline) {
                    entradaInline.setLength(0);
                    cursorInline = 0;
                    selecionarTudoInline = false;
                } else if (cursorInline > 0) {
                    entradaInline.deleteCharAt(--cursorInline);
                }
            }
            case Delete -> {
                if (selecionarTudoInline) {
                    entradaInline.setLength(0);
                    cursorInline = 0;
                    selecionarTudoInline = false;
                } else if (cursorInline < entradaInline.length()) {
                    entradaInline.deleteCharAt(cursorInline);
                }
            }
            case ArrowLeft -> {
                selecionarTudoInline = false;
                cursorInline = Math.max(0, cursorInline - 1);
            }
            case ArrowRight -> {
                selecionarTudoInline = false;
                cursorInline = Math.min(entradaInline.length(), cursorInline + 1);
            }
            case Home -> {
                selecionarTudoInline = false;
                cursorInline = 0;
            }
            case End -> {
                selecionarTudoInline = false;
                cursorInline = entradaInline.length();
            }
            case Enter -> concluirEdicaoInline(entradaInline.toString());
            default -> { }
        }
        return true;
    }

    private boolean processarConfirmacao(KeyStroke tecla) {
        if (tecla.getKeyType() == KeyType.Enter || caractere(tecla, 's')) {
            Runnable acao = acaoConfirmacao;
            String mensagem = mensagemConfirmacao;
            encerrarConfirmacao();
            acao.run();
            mostrarAvisoTemporario(mensagem);
        } else if (tecla.getKeyType() == KeyType.Escape || caractere(tecla, 'n')) {
            encerrarConfirmacao();
            mostrarAvisoTemporario("Operação cancelada.");
        }
        return true;
    }

    private boolean processarOpcaoRapida(KeyStroke tecla) {
        if (tecla.getKeyType() == KeyType.ArrowLeft || tecla.getKeyType() == KeyType.ArrowUp) {
            opcaoRapida = circular(opcaoRapida - 1, opcoesRapidas.length);
            atualizarAvisoOpcaoRapida();
        } else if (tecla.getKeyType() == KeyType.ArrowRight || tecla.getKeyType() == KeyType.ArrowDown) {
            opcaoRapida = circular(opcaoRapida + 1, opcoesRapidas.length);
            atualizarAvisoOpcaoRapida();
        } else if (tecla.getKeyType() == KeyType.Enter) {
            AcaoEdicao acao = acaoOpcaoRapida;
            String valor = opcoesRapidas[opcaoRapida];
            encerrarOpcaoRapida();
            try {
                acao.executar(valor);
            } catch (IllegalArgumentException e) {
                mostrarErroPopup(e.getMessage());
            }
        } else if (tecla.getKeyType() == KeyType.Escape) {
            encerrarOpcaoRapida();
            mostrarAvisoTemporario("Operação cancelada.");
        }
        return true;
    }

    private boolean processarPopupErro(KeyStroke tecla) {
        if (tecla.getKeyType() == KeyType.Enter || tecla.getKeyType() == KeyType.Escape) {
            popupErroAberto = false;
            mensagemPopupErro = null;
        }
        return true;
    }

    private boolean processarPopupSaida(KeyStroke tecla) {
        if (tecla.getKeyType() == KeyType.ArrowLeft || tecla.getKeyType() == KeyType.ArrowUp) {
            opcaoPopupSaida = circular(opcaoPopupSaida - 1, 2);
        } else if (tecla.getKeyType() == KeyType.ArrowRight || tecla.getKeyType() == KeyType.ArrowDown) {
            opcaoPopupSaida = circular(opcaoPopupSaida + 1, 2);
        } else if (tecla.getKeyType() == KeyType.Enter) {
            if (opcaoPopupSaida == 0) return false;
            popupSaidaAberto = false;
        } else if (tecla.getKeyType() == KeyType.Escape) {
            popupSaidaAberto = false;
        }
        return true;
    }

    private void abrirPopupSaida() {
        popupSaidaAberto = true;
        opcaoPopupSaida = 1;
    }

    private boolean navegar(KeyStroke tecla, int total) {
        if (total <= 0) {
            selecao = 0;
            return false;
        }
        if (tecla.getKeyType() == KeyType.ArrowUp) {
            selecao = circular(selecao - 1, total);
            return true;
        }
        if (tecla.getKeyType() == KeyType.ArrowDown) {
            selecao = circular(selecao + 1, total);
            return true;
        }
        selecao = Math.min(selecao, total - 1);
        return false;
    }

    private void editarDado() {
        Ficha ficha = fichaService.carregarFicha();
        String atual = valoresDados(ficha).get(selecao);
        iniciarEdicaoInline(atual, valor -> {
            if (campoDadoNumerico(selecao)) aplicarDadoNumerico(ficha, selecao, inteiro(valor, 0));
            else aplicarDadoTexto(ficha, selecao, textoObrigatorio(valor));
            fichaService.salvarFicha(ficha);
        });
    }

    private void aplicarDadoNumerico(Ficha ficha, int campo, int valor) {
        switch (campo) {
            case 1 -> ficha.setIdade(valor);
            case 3 -> fichaService.atualizarVidaMaxima(ficha, valor);
            case 4 -> fichaService.atualizarVidaTemporaria(ficha, valor);
            case 5 -> fichaService.atualizarVidaAtual(ficha, valor);
            case 9 -> ficha.setNivel(valor);
            case 10 -> ficha.setMoedaBronze(valor);
            case 11 -> ficha.setMoedaPrata(valor);
            case 12 -> ficha.setMoedaOuro(valor);
            default -> throw new IllegalArgumentException("Campo numérico inválido.");
        }
    }

    private void aplicarDadoTexto(Ficha ficha, int campo, String valor) {
        switch (campo) {
            case 0 -> ficha.setNome(valor);
            case 2 -> ficha.setSexo(valor);
            case 6 -> ficha.setStatus(valor);
            case 7 -> ficha.setRaca(valor);
            case 8 -> ficha.setClasse(valor);
            default -> throw new IllegalArgumentException("Campo de texto inválido.");
        }
    }

    private void editarAtributo() {
        Ficha ficha = fichaService.carregarFicha();
        Atributos atributos = ficha.getAtributos();
        int[] atuais = valoresAtributos(atributos);
        iniciarEdicaoInline(String.valueOf(atuais[selecao]), valor -> {
            int numero = inteiro(valor, 1);
            switch (selecao) {
                case 0 -> atributos.setBonusProficiencia(numero);
                case 1 -> atributos.setInteligencia(numero);
                case 2 -> atributos.setSabedoria(numero);
                case 3 -> atributos.setForca(numero);
                case 4 -> atributos.setDestreza(numero);
                case 5 -> atributos.setConstituicao(numero);
                case 6 -> atributos.setCarisma(numero);
                default -> throw new IllegalArgumentException("Atributo inválido.");
            }
            fichaService.salvarFicha(ficha);
        });
    }

    private void abrirEditor(Tela editor) {
        adicionandoRegistro = false;
        rascunho = null;
        indiceRegistroEdicao = selecao;
        selecao = 0;
        tela = editor;
        aviso = "Selecione um campo e pressione Enter para editar.";
        avisoExpiraEm = 0;
    }

    private void abrirAdicao(Tela editor, String[] valoresIniciais) {
        adicionandoRegistro = true;
        rascunho = valoresIniciais;
        selecao = 0;
        tela = editor;
        aviso = "Preencha os campos. Pressione S para salvar ou Esc para cancelar.";
        avisoExpiraEm = 0;
    }

    private void abrirPermissoesEquipamento() {
        indiceRegistroEdicao = selecao;
        selecao = 0;
        permissoesRascunho = equipamentoService.carregarPermissoes().clone();
        tela = Tela.PERMISSOES_EQUIPAMENTO;
        aviso = "";
        avisoExpiraEm = 0;
    }

    private void voltarParaLista(Tela lista) {
        tela = lista;
        selecao = adicionandoRegistro ? 0 : indiceRegistroEdicao;
        adicionandoRegistro = false;
        rascunho = null;
        aviso = "";
        avisoExpiraEm = 0;
    }

    private void iniciarEdicaoCampoItem() {
        if (adicionandoRegistro) {
            iniciarCampoRascunho(selecao == 2 ? TIPOS_ITEM : null);
            return;
        }
        Item atual = itemService.buscarPorIndice(indiceRegistroEdicao);
        if (selecao == 2) {
            iniciarOpcoesInline(atual.getTipo(), TIPOS_ITEM, valor -> salvarItemEditado(atual, valor));
            return;
        }
        iniciarEdicaoInline(valoresItem(atual).get(selecao), valor -> salvarItemEditado(atual, valor));
    }

    private void salvarItemEditado(Item atual, String valor) {
        Item editado = switch (selecao) {
            case 0 -> new Item(textoObrigatorio(valor), atual.getQuantidade(), atual.getTipo(), atual.getDescricao());
            case 1 -> new Item(atual.getNome(), inteiro(valor, 1), atual.getTipo(), atual.getDescricao());
            case 2 -> new Item(atual.getNome(), atual.getQuantidade(), valor, atual.getDescricao());
            case 3 -> new Item(atual.getNome(), atual.getQuantidade(), atual.getTipo(), textoObrigatorio(valor));
            default -> throw new IllegalArgumentException("Campo de item inválido.");
        };
        itemService.editarItem(indiceRegistroEdicao, editado);
    }

    private void iniciarEdicaoCampoEquipamento() {
        if (adicionandoRegistro) {
            String[] opcoes = selecao == 1
                ? TIPOS_EQUIPAMENTO
                : selecao == 2 && "Armadura".equals(rascunho[1])
                    ? CATEGORIAS_ARMADURA
                    : selecao == 7 ? RARIDADES : null;
            iniciarCampoRascunho(opcoes);
            return;
        }
        Equipamento atual = equipamentoService.buscarPorIndice(indiceRegistroEdicao);
        if (selecao == 1) {
            iniciarOpcoesInline(atual.getTipo(), TIPOS_EQUIPAMENTO, valor -> salvarEquipamentoEditado(atual, valor));
        } else if (selecao == 2 && "Armadura".equals(atual.getTipo())) {
            iniciarOpcoesInline(atual.getCategoria(), CATEGORIAS_ARMADURA, valor -> salvarEquipamentoEditado(atual, valor));
        } else if (selecao == 7) {
            iniciarOpcoesInline(atual.getRaridade(), RARIDADES, valor -> salvarEquipamentoEditado(atual, valor));
        } else {
            iniciarEdicaoInline(valoresEquipamento(atual).get(selecao), valor -> salvarEquipamentoEditado(atual, valor));
        }
    }

    private void salvarEquipamentoEditado(Equipamento atual, String valor) {
        String nome = atual.getNome();
        String tipo = atual.getTipo();
        String categoria = atual.getCategoria();
        String dano = atual.getDano();
        String tipoDano = atual.getTipoDano();
        String peso = atual.getPeso();
        int moedas = atual.getValor();
        String raridade = atual.getRaridade();

        switch (selecao) {
            case 0 -> nome = textoObrigatorio(valor);
            case 1 -> tipo = valor;
            case 2 -> categoria = textoObrigatorio(valor);
            case 3 -> dano = textoObrigatorio(valor);
            case 4 -> tipoDano = textoObrigatorio(valor);
            case 5 -> peso = textoObrigatorio(valor);
            case 6 -> moedas = inteiro(valor, 1);
            case 7 -> raridade = valor;
            default -> throw new IllegalArgumentException("Campo de equipamento inválido.");
        }
        String equipadoComo = selecao == 1 && !tipo.equals(atual.getTipo()) ? "" : atual.getEquipadoComo();
        equipamentoService.editar(
            indiceRegistroEdicao,
            new Equipamento(nome, tipo, categoria, dano, tipoDano, peso, moedas, raridade, equipadoComo)
        );
    }

    private void iniciarEdicaoCampoHabilidade() {
        if (adicionandoRegistro) {
            iniciarCampoRascunho(null);
            return;
        }
        Habilidade atual = habilidadeService.buscarPorIndice(indiceRegistroEdicao);
        iniciarEdicaoInline(valoresHabilidade(atual).get(selecao), valor -> {
            String nome = selecao == 0 ? textoObrigatorio(valor) : atual.getNome();
            int nivel = selecao == 1 ? inteiro(valor, 0) : atual.getNivel();
            String tipo = selecao == 2 ? textoObrigatorio(valor) : atual.getTipo();
            String alcance = selecao == 3 ? textoObrigatorio(valor) : atual.getAlcance();
            String recarga = selecao == 4 ? textoObrigatorio(valor) : atual.getRecarga();
            String custo = selecao == 5 ? textoObrigatorio(valor) : atual.getCusto();
            String descricao = selecao == 6 ? textoObrigatorio(valor) : atual.getDescricao();
            habilidadeService.editarHabilidade(
                indiceRegistroEdicao,
                new Habilidade(nome, nivel, tipo, alcance, recarga, custo, descricao)
            );
        });
    }

    private void iniciarEdicaoCampoAnotacao() {
        if (adicionandoRegistro) {
            iniciarCampoRascunho(null);
            return;
        }
        List<Anotacao> anotacoes = new ArrayList<>(anotacaoService.listar());
        Anotacao atual = anotacoes.get(indiceRegistroEdicao);
        String valorAtual = selecao == 0 ? atual.getTitulo() : atual.getTexto();
        iniciarEdicaoInline(valorAtual, valor -> {
            Anotacao editada = selecao == 0
                ? new Anotacao(textoObrigatorio(valor), atual.getTexto())
                : new Anotacao(atual.getTitulo(), textoObrigatorio(valor));
            anotacoes.set(indiceRegistroEdicao, editada);
            anotacaoService.salvar(anotacoes);
        });
    }

    private void iniciarCampoRascunho(String[] opcoes) {
        AcaoEdicao salvarCampo = valor -> rascunho[selecao] = valor;
        if (opcoes == null) iniciarEdicaoInline(rascunho[selecao], salvarCampo);
        else iniciarOpcoesInline(rascunho[selecao], opcoes, salvarCampo);
    }

    private void salvarNovoItem() {
        try {
            Item item = new Item(
                textoObrigatorio(rascunho[0]),
                inteiro(rascunho[1], 1),
                textoObrigatorio(rascunho[2]),
                textoObrigatorio(rascunho[3])
            );
            itemService.adicionarItem(item);
            finalizarAdicao(Tela.ITENS, itemService.listarItens().size());
        } catch (IllegalArgumentException e) {
            erroNoFormulario(e);
        }
    }

    private void salvarNovoEquipamento() {
        try {
            Equipamento equipamento = new Equipamento(
                textoObrigatorio(rascunho[0]),
                textoObrigatorio(rascunho[1]),
                textoObrigatorio(rascunho[2]),
                textoObrigatorio(rascunho[3]),
                textoObrigatorio(rascunho[4]),
                textoObrigatorio(rascunho[5]),
                inteiro(rascunho[6], 1),
                textoObrigatorio(rascunho[7]),
                ""
            );
            equipamentoService.adicionar(equipamento);
            finalizarAdicao(Tela.EQUIPAMENTOS, equipamentoService.listar().size());
        } catch (IllegalArgumentException e) {
            erroNoFormulario(e);
        }
    }

    private void salvarNovaHabilidade() {
        try {
            Habilidade habilidade = new Habilidade(
                textoObrigatorio(rascunho[0]),
                inteiro(rascunho[1], 0),
                textoObrigatorio(rascunho[2]),
                textoObrigatorio(rascunho[3]),
                textoObrigatorio(rascunho[4]),
                textoObrigatorio(rascunho[5]),
                textoObrigatorio(rascunho[6])
            );
            habilidadeService.adicionarHabilidade(habilidade);
            finalizarAdicao(Tela.HABILIDADES, habilidadeService.listarHabilidades().size());
        } catch (IllegalArgumentException e) {
            erroNoFormulario(e);
        }
    }

    private void salvarNovaAnotacao() {
        try {
            List<Anotacao> anotacoes = new ArrayList<>(anotacaoService.listar());
            anotacoes.add(new Anotacao(textoObrigatorio(rascunho[0]), textoObrigatorio(rascunho[1])));
            anotacaoService.salvar(anotacoes);
            finalizarAdicao(Tela.ANOTACOES, anotacoes.size());
        } catch (IllegalArgumentException e) {
            erroNoFormulario(e);
        }
    }

    private void finalizarAdicao(Tela lista, int total) {
        tela = lista;
        adicionandoRegistro = false;
        rascunho = null;
        selecionarUltimo(total);
        mostrarAvisoTemporario("Registro adicionado com sucesso.");
    }

    private void erroNoFormulario(IllegalArgumentException e) {
        mostrarErroPopup(e.getMessage());
    }

    private void iniciarEdicaoInline(String atual, AcaoEdicao acao) {
        editandoInline = true;
        entradaInline = new StringBuilder(atual == null ? "" : atual);
        cursorInline = entradaInline.length();
        selecionarTudoInline = true;
        opcoesInline = null;
        acaoEdicao = acao;
        aviso = "Digite o valor. Enter salva e Esc cancela.";
        avisoExpiraEm = 0;
    }

    private void iniciarOpcoesInline(String atual, String[] opcoes, AcaoEdicao acao) {
        editandoInline = true;
        selecionarTudoInline = false;
        opcoesInline = opcoes;
        opcaoInline = indiceOpcao(opcoes, atual);
        acaoEdicao = acao;
        aviso = "Use ←/→ para alterar. Enter salva e Esc cancela.";
        avisoExpiraEm = 0;
    }

    private void concluirEdicaoInline(String valor) {
        try {
            acaoEdicao.executar(valor.trim());
            cancelarEdicaoInline();
            mostrarAvisoTemporario(adicionandoRegistro ? "Campo preenchido." : "Alteração salva com sucesso.");
        } catch (IllegalArgumentException e) {
            mostrarErroPopup(e.getMessage());
        }
    }

    private void cancelarEdicaoInline() {
        editandoInline = false;
        opcoesInline = null;
        acaoEdicao = null;
    }

    private void iniciarConfirmacao(String pergunta, String mensagemSucesso, Runnable acao) {
        confirmando = true;
        acaoConfirmacao = acao;
        mensagemConfirmacao = mensagemSucesso;
        aviso = pergunta + "  Enter/S confirma   Esc/N cancela";
        avisoExpiraEm = 0;
    }

    private void encerrarConfirmacao() {
        confirmando = false;
        acaoConfirmacao = null;
        mensagemConfirmacao = null;
        aviso = "";
    }

    private void iniciarOpcaoRapida(String descricao, String[] opcoes, AcaoEdicao acao) {
        escolhendoOpcaoRapida = true;
        descricaoOpcaoRapida = descricao;
        opcoesRapidas = opcoes;
        opcaoRapida = 0;
        acaoOpcaoRapida = acao;
        avisoExpiraEm = 0;
        atualizarAvisoOpcaoRapida();
    }

    private void atualizarAvisoOpcaoRapida() {
        aviso = "";
    }

    private void encerrarOpcaoRapida() {
        escolhendoOpcaoRapida = false;
        descricaoOpcaoRapida = null;
        opcoesRapidas = null;
        acaoOpcaoRapida = null;
        aviso = "";
    }

    private void mostrarErroPopup(String mensagem) {
        popupErroAberto = true;
        mensagemPopupErro = mensagem;
    }

    private void mostrarAvisoTemporario(String texto) {
        aviso = texto;
        avisoExpiraEm = System.currentTimeMillis() + 1_000;
    }

    private boolean expirarAviso() {
        if (avisoExpiraEm == 0 || System.currentTimeMillis() < avisoExpiraEm) return false;
        aviso = "";
        avisoExpiraEm = 0;
        return true;
    }

    private String textoObrigatorio(String valor) {
        String texto = valor == null ? "" : valor.trim();
        if (texto.isEmpty()) throw new IllegalArgumentException("Digite um texto válido.");
        return texto;
    }

    private int inteiro(String valor, int minimo) {
        try {
            int numero = Integer.parseInt(valor.trim());
            if (numero >= minimo) return numero;
        } catch (NumberFormatException e) {
            // A mensagem abaixo cobre texto e números fora do intervalo.
        }
        throw new IllegalArgumentException(
            minimo == 1 ? "Digite um número inteiro positivo." : "Digite um número maior ou igual a " + minimo + "."
        );
    }

    private int indiceOpcao(String[] opcoes, String atual) {
        for (int i = 0; i < opcoes.length; i++) {
            if (opcoes[i].equalsIgnoreCase(atual)) return i;
        }
        return 0;
    }

    private void excluirItem() {
        Item item = itemService.buscarPorIndice(selecao);
        iniciarConfirmacao(
            "Excluir \"" + item.getNome() + "\"?",
            "Item excluído com sucesso.",
            () -> {
                itemService.excluirItem(selecao);
                ajustarSelecao(itemService.listarItens().size());
            }
        );
    }

    private void alternarEquipamento() {
        Equipamento equipamento = equipamentoService.buscarPorIndice(selecao);
        if ("Arma".equals(equipamento.getTipo()) && equipamento.getEquipadoComo().isEmpty()) {
            iniciarOpcaoRapida(
                "Slot da arma",
                new String[] {"Principal", "Secundária"},
                slot -> mostrarAvisoTemporario(equipamentoService.alternarEquipado(selecao, slot))
            );
            return;
        }
        try {
            mostrarAvisoTemporario(equipamentoService.alternarEquipado(selecao, null));
        } catch (IllegalArgumentException e) {
            mostrarErroPopup(e.getMessage());
        }
    }

    private void excluirHabilidade() {
        Habilidade habilidade = habilidadeService.buscarPorIndice(selecao);
        iniciarConfirmacao(
            "Excluir \"" + habilidade.getNome() + "\"?",
            "Habilidade excluída com sucesso.",
            () -> {
                habilidadeService.excluirHabilidade(selecao);
                ajustarSelecao(habilidadeService.listarHabilidades().size());
            }
        );
    }

    private void excluirAnotacao() {
        List<Anotacao> anotacoes = new ArrayList<>(anotacaoService.listar());
        Anotacao anotacao = anotacoes.get(selecao);
        iniciarConfirmacao(
            "Excluir \"" + anotacao.getTitulo() + "\"?",
            "Anotação excluída com sucesso.",
            () -> {
                anotacoes.remove(selecao);
                anotacaoService.salvar(anotacoes);
                ajustarSelecao(anotacoes.size());
            }
        );
    }

    private void desenhar(Screen screen) throws IOException {
        screen.clear();
        cursorTelaX = 0;
        cursorTelaY = 0;
        TextGraphics graphics = screen.newTextGraphics();
        TerminalSize tamanho = screen.getTerminalSize();

        switch (tela) {
            case PRINCIPAL -> desenharPrincipal(graphics, tamanho);
            case DADOS -> desenharDados(graphics, tamanho);
            case ATRIBUTOS -> desenharAtributos(graphics, tamanho);
            case ITENS -> desenharItens(graphics, tamanho, false);
            case EDICAO_ITEM -> desenharEditorItem(graphics, tamanho);
            case INVENTARIO -> desenharItens(graphics, tamanho, true);
            case DETALHE_ITEM -> desenharDetalheItem(graphics, tamanho);
            case EQUIPAMENTOS -> desenharEquipamentos(graphics, tamanho);
            case EDICAO_EQUIPAMENTO -> desenharEditorEquipamento(graphics, tamanho);
            case PERMISSOES_EQUIPAMENTO -> desenharPermissoesEquipamento(graphics, tamanho);
            case DETALHE_EQUIPAMENTO -> desenharDetalheEquipamento(graphics, tamanho);
            case HABILIDADES -> desenharHabilidades(graphics, tamanho);
            case EDICAO_HABILIDADE -> desenharEditorHabilidade(graphics, tamanho);
            case DETALHE_HABILIDADE -> desenharDetalheHabilidade(graphics, tamanho);
            case ANOTACOES -> desenharAnotacoes(graphics, tamanho);
            case EDICAO_ANOTACAO -> desenharEditorAnotacao(graphics, tamanho);
            case DETALHE_ANOTACAO -> desenharDetalheAnotacao(graphics, tamanho);
        }
        if (popupSaidaAberto) desenharPopupSaida(graphics, tamanho);
        else if (popupErroAberto) desenharPopupErro(graphics, tamanho);
        else if (escolhendoOpcaoRapida) desenharPopupOpcao(graphics, tamanho);
        boolean exibirCursor = editandoInline
            && !popupSaidaAberto
            && !popupErroAberto
            && !escolhendoOpcaoRapida;
        screen.setCursorPosition(exibirCursor ? new TerminalPosition(cursorTelaX, cursorTelaY) : null);
        screen.refresh();
    }

    private void desenharPopupErro(TextGraphics graphics, TerminalSize tamanho) {
        int largura = Math.min(64, Math.max(24, tamanho.getColumns() - 4));
        int altura = 8;
        int x = Math.max(0, (tamanho.getColumns() - largura) / 2);
        int y = Math.max(0, (tamanho.getRows() - altura) / 2);
        desenharCaixaPopup(graphics, x, y, largura, altura, "ERRO");
        escreverQuebrado(graphics, x + 2, y + 2, mensagemPopupErro, largura - 4, 3);
        escreverCentralizadoNaArea(graphics, y + altura - 2, "[ OK ]", x, largura, SGR.BOLD);
    }

    private void desenharPopupSaida(TextGraphics graphics, TerminalSize tamanho) {
        int largura = Math.min(56, Math.max(32, tamanho.getColumns() - 4));
        int altura = 9;
        int x = Math.max(0, (tamanho.getColumns() - largura) / 2);
        int y = Math.max(0, (tamanho.getRows() - altura) / 2);
        desenharCaixaPopup(graphics, x, y, largura, altura, "CONFIRMAR SAÍDA");
        escreverCentralizadoNaArea(
            graphics, y + 2, "Deseja realmente sair do sistema?", x, largura, SGR.BOLD
        );
        escreverSelecionado(
            graphics, x + 5, y + 4, "Sair", opcaoPopupSaida == 0, largura - 10
        );
        escreverSelecionado(
            graphics, x + 5, y + 5, "Cancelar", opcaoPopupSaida == 1, largura - 10
        );
        escreverCentralizadoNaArea(
            graphics, y + altura - 2, "↑↓/←→ Selecionar   Enter Confirmar   Esc Cancelar", x, largura
        );
    }

    private void desenharPopupOpcao(TextGraphics graphics, TerminalSize tamanho) {
        int largura = Math.min(68, Math.max(28, tamanho.getColumns() - 4));
        int altura = Math.min(11, 7 + opcoesRapidas.length);
        int x = Math.max(0, (tamanho.getColumns() - largura) / 2);
        int y = Math.max(0, (tamanho.getRows() - altura) / 2);
        desenharCaixaPopup(graphics, x, y, largura, altura, descricaoOpcaoRapida.toUpperCase());
        for (int i = 0; i < opcoesRapidas.length; i++) {
            escreverSelecionado(
                graphics, x + 3, y + 2 + i, opcoesRapidas[i], i == opcaoRapida, largura - 6
            );
        }
        escreverCentralizadoNaArea(
            graphics, y + altura - 2, "↑↓/←→ Selecionar   Enter Confirmar   Esc Cancelar", x, largura
        );
    }

    private void desenharCaixaPopup(
        TextGraphics graphics,
        int x,
        int y,
        int largura,
        int altura,
        String titulo
    ) {
        graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
        String vazio = " ".repeat(largura);
        for (int linha = 0; linha < altura; linha++) escrever(graphics, x, y + linha, vazio, largura);
        escrever(graphics, x, y, "┌" + "─".repeat(largura - 2) + "┐", largura);
        escrever(graphics, x, y + altura - 1, "└" + "─".repeat(largura - 2) + "┘", largura);
        for (int linha = 1; linha < altura - 1; linha++) {
            escrever(graphics, x, y + linha, "│", 1);
            escrever(graphics, x + largura - 1, y + linha, "│", 1);
        }
        escreverCentralizadoNaArea(graphics, y, " " + titulo + " ", x, largura, SGR.BOLD);
    }

    private void escreverCentralizadoNaArea(
        TextGraphics graphics,
        int y,
        String texto,
        int x,
        int largura,
        SGR... estilos
    ) {
        int posicao = x + Math.max(0, (largura - texto.length()) / 2);
        escrever(graphics, posicao, y, texto, x + largura - posicao, estilos);
    }

    private void desenharPrincipal(TextGraphics graphics, TerminalSize tamanho) {
        desenharMoldura(graphics, tamanho, "FICHA DO PERSONAGEM");
        Ficha ficha = fichaService.carregarFicha();
        int largura = tamanho.getColumns();

        escrever(graphics, 2, 2, vazio(ficha.getNome(), "Personagem sem nome"), largura - 4, SGR.BOLD);
        escreverDireita(graphics, 2, "Nível " + ficha.getNivel(), largura);
        escrever(graphics, 2, 3, vazio(ficha.getClasse(), "Sem classe") + " • " + vazio(ficha.getRaca(), "Sem raça"), largura - 4);
        escrever(graphics, 2, 5, vidaFormatada(ficha), largura - 4, SGR.BOLD);
        escreverDireita(graphics, 5, "Status: " + vazio(ficha.getStatus(), "-"), largura);
        escrever(
            graphics, 2, 7,
            "Moedas  Bronze: " + ficha.getMoedaBronze() + "   Prata: " + ficha.getMoedaPrata() + "   Ouro: " + ficha.getMoedaOuro(),
            largura - 4
        );
        desenharSeparador(graphics, 9, largura, "MENU PRINCIPAL");
        for (int i = 0; i < OPCOES.length && 10 + i < tamanho.getRows() - 2; i++) {
            escreverSelecionado(graphics, 3, 10 + i, OPCOES[i], i == opcaoSelecionada, largura - 6);
        }
        escreverRodape(graphics, tamanho, "↑↓ Navegar   Enter Selecionar   Esc/Q Sair");
    }

    private void desenharDados(TextGraphics graphics, TerminalSize tamanho) {
        desenharMoldura(graphics, tamanho, "DADOS DO PERSONAGEM");
        desenharCamposInline(graphics, tamanho, CAMPOS_DADOS, valoresDados(fichaService.carregarFicha()), 2);
        escreverRodape(graphics, tamanho, rodapeEdicao("↑↓ Navegar   Enter Editar   Esc Voltar   Q Sair"));
    }

    private void desenharAtributos(TextGraphics graphics, TerminalSize tamanho) {
        desenharMoldura(graphics, tamanho, "ATRIBUTOS");
        int[] valores = valoresAtributos(fichaService.carregarFicha().getAtributos());
        int largura = tamanho.getColumns();
        escreverCampoInline(graphics, 3, 2, CAMPOS_ATRIBUTOS[0], String.valueOf(valores[0]), selecao == 0, largura - 6, SGR.BOLD);
        desenharSeparador(graphics, 4, largura, "CARACTERÍSTICAS");
        for (int i = 1; i < CAMPOS_ATRIBUTOS.length; i++) {
            escreverCampoInline(graphics, 3, 5 + i, CAMPOS_ATRIBUTOS[i], String.valueOf(valores[i]), i == selecao, largura - 6);
        }
        escreverRodape(graphics, tamanho, rodapeEdicao("↑↓ Navegar   Enter Editar   Esc Voltar   Q Sair"));
    }

    private void desenharItens(TextGraphics graphics, TerminalSize tamanho, boolean inventario) {
        desenharMoldura(graphics, tamanho, inventario ? "INVENTÁRIO" : "ITENS");
        List<String> linhas = new ArrayList<>();
        for (Item item : itemService.listarItens()) {
            linhas.add(item.getNome() + "  x" + item.getQuantidade() + "  [" + item.getTipo() + "]");
        }
        desenharLista(graphics, tamanho, linhas, inventario ? "O inventário está vazio." : "Nenhum item encontrado.");
        escreverRodape(
            graphics, tamanho,
            inventario
                ? "↑↓ Navegar   Enter Detalhes   Esc Voltar   Q Sair"
                : rodapeEdicao("↑↓ Navegar   Enter Detalhes   A Adicionar   E Editar   Del Excluir   Esc Voltar")
        );
    }

    private void desenharEquipamentos(TextGraphics graphics, TerminalSize tamanho) {
        desenharMoldura(graphics, tamanho, "EQUIPAMENTOS");
        List<String> linhas = new ArrayList<>();
        for (Equipamento equipamento : equipamentoService.listar()) {
            String slot = equipamento.getEquipadoComo().isEmpty() ? "" : "  [" + equipamento.getEquipadoComo() + "]";
            linhas.add(equipamento.getNome() + "  • " + equipamento.getTipo() + slot);
        }
        desenharLista(graphics, tamanho, linhas, "Nenhum equipamento encontrado.");
        escreverRodape(
            graphics, tamanho,
            rodapeEdicao("↑↓ Enter Detalhes   A Adicionar   E Editar   X Equipar   P Permissões   Esc Voltar")
        );
    }

    private void desenharPermissoesEquipamento(TextGraphics graphics, TerminalSize tamanho) {
        desenharMoldura(graphics, tamanho, "PERMISSÕES DE ARMADURA");
        int largura = tamanho.getColumns();
        escrever(
            graphics, 3, 2,
            "Defina quais categorias o personagem pode equipar.",
            largura - 6
        );
        desenharSeparador(graphics, 4, largura, "CATEGORIAS");
        for (int i = 0; i < CATEGORIAS_ARMADURA.length; i++) {
            String estado = permissoesRascunho[i] ? "Permitida" : "Não permitida";
            escreverSelecionado(
                graphics, 3, 6 + i,
                CATEGORIAS_ARMADURA[i] + ": " + estado,
                i == selecao,
                largura - 6
            );
        }
        escreverRodape(
            graphics, tamanho,
            rodapeEdicao("↑↓ Categoria   Enter/←→ Alterar   S Salvar   Esc Cancelar")
        );
    }

    private void desenharHabilidades(TextGraphics graphics, TerminalSize tamanho) {
        desenharMoldura(graphics, tamanho, "HABILIDADES");
        List<String> linhas = new ArrayList<>();
        for (Habilidade habilidade : habilidadeService.listarHabilidades()) {
            linhas.add(habilidade.getNome() + "  • Nível " + habilidade.getNivel() + "  [" + habilidade.getTipo() + "]");
        }
        desenharLista(graphics, tamanho, linhas, "Nenhuma habilidade encontrada.");
        escreverRodape(
            graphics, tamanho,
            rodapeEdicao("↑↓ Enter Detalhes   A Adicionar   E Editar   Del Excluir   Esc Voltar")
        );
    }

    private void desenharAnotacoes(TextGraphics graphics, TerminalSize tamanho) {
        desenharMoldura(graphics, tamanho, "ANOTAÇÕES");
        List<String> linhas = new ArrayList<>();
        for (Anotacao anotacao : anotacaoService.listar()) linhas.add(anotacao.getTitulo());
        desenharLista(graphics, tamanho, linhas, "Nenhuma anotação encontrada.");
        escreverRodape(
            graphics, tamanho,
            rodapeEdicao("↑↓ Enter Visualizar   A Adicionar   E Editar   Del Excluir   Esc Voltar")
        );
    }

    private void desenharEditorItem(TextGraphics graphics, TerminalSize tamanho) {
        if (adicionandoRegistro) {
            desenharEditor(graphics, tamanho, "ADICIONAR ITEM", "Novo item", CAMPOS_ITEM, List.of(rascunho));
            return;
        }
        Item item = itemService.buscarPorIndice(indiceRegistroEdicao);
        desenharEditor(
            graphics, tamanho, "EDITAR ITEM", item.getNome(), CAMPOS_ITEM, valoresItem(item)
        );
    }

    private void desenharEditorEquipamento(TextGraphics graphics, TerminalSize tamanho) {
        if (adicionandoRegistro) {
            desenharEditor(
                graphics, tamanho, "ADICIONAR EQUIPAMENTO", "Novo equipamento",
                CAMPOS_EQUIPAMENTO, List.of(rascunho)
            );
            return;
        }
        Equipamento equipamento = equipamentoService.buscarPorIndice(indiceRegistroEdicao);
        desenharEditor(
            graphics, tamanho, "EDITAR EQUIPAMENTO", equipamento.getNome(),
            CAMPOS_EQUIPAMENTO, valoresEquipamento(equipamento)
        );
    }

    private void desenharEditorHabilidade(TextGraphics graphics, TerminalSize tamanho) {
        if (adicionandoRegistro) {
            desenharEditor(
                graphics, tamanho, "ADICIONAR HABILIDADE", "Nova habilidade",
                CAMPOS_HABILIDADE, List.of(rascunho)
            );
            return;
        }
        Habilidade habilidade = habilidadeService.buscarPorIndice(indiceRegistroEdicao);
        desenharEditor(
            graphics, tamanho, "EDITAR HABILIDADE", habilidade.getNome(),
            CAMPOS_HABILIDADE, valoresHabilidade(habilidade)
        );
    }

    private void desenharEditorAnotacao(TextGraphics graphics, TerminalSize tamanho) {
        if (adicionandoRegistro) {
            desenharEditor(
                graphics, tamanho, "ADICIONAR ANOTAÇÃO", "Nova anotação",
                CAMPOS_ANOTACAO, List.of(rascunho)
            );
            return;
        }
        Anotacao anotacao = anotacaoService.listar().get(indiceRegistroEdicao);
        desenharEditor(
            graphics, tamanho, "EDITAR ANOTAÇÃO", anotacao.getTitulo(),
            CAMPOS_ANOTACAO, List.of(anotacao.getTitulo(), anotacao.getTexto())
        );
    }

    private void desenharEditor(
        TextGraphics graphics,
        TerminalSize tamanho,
        String titulo,
        String registro,
        String[] campos,
        List<String> valores
    ) {
        desenharMoldura(graphics, tamanho, titulo);
        int largura = tamanho.getColumns();
        escrever(graphics, 3, 2, registro, largura - 6, SGR.BOLD);
        desenharSeparador(graphics, 3, largura, "CAMPOS");
        desenharCamposInline(graphics, tamanho, campos, valores, 5);
        String rodape = adicionandoRegistro
            ? "↑↓ Campo   Enter Preencher   S Salvar   Esc Cancelar"
            : "↑↓ Campo   Enter Editar   Esc Voltar";
        escreverRodape(graphics, tamanho, rodapeEdicao(rodape));
    }

    private void desenharDetalheItem(TextGraphics graphics, TerminalSize tamanho) {
        List<Item> itens = itemService.listarItens();
        if (itens.isEmpty()) { tela = Tela.ITENS; return; }
        ajustarSelecao(itens.size());
        Item item = itens.get(selecao);
        desenharDetalhes(
            graphics, tamanho, "ITEM", item.getNome(),
            List.of("Quantidade: " + item.getQuantidade(), "Tipo: " + item.getTipo()),
            "Descrição", item.getDescricao()
        );
    }

    private void desenharDetalheEquipamento(TextGraphics graphics, TerminalSize tamanho) {
        List<Equipamento> equipamentos = equipamentoService.listar();
        if (equipamentos.isEmpty()) { tela = Tela.EQUIPAMENTOS; return; }
        ajustarSelecao(equipamentos.size());
        Equipamento equipamento = equipamentos.get(selecao);
        List<String> campos = List.of(
            "Tipo: " + equipamento.getTipo(), "Categoria: " + equipamento.getCategoria(),
            "Dano: " + equipamento.getDano(), "Tipo de dano: " + equipamento.getTipoDano(),
            "Peso: " + equipamento.getPeso() + " kg", "Valor: " + equipamento.getValor() + " moedas",
            "Raridade: " + equipamento.getRaridade(),
            "Equipado: " + (equipamento.getEquipadoComo().isEmpty() ? "Não" : "Sim (" + equipamento.getEquipadoComo() + ")")
        );
        desenharDetalhes(graphics, tamanho, "EQUIPAMENTO", equipamento.getNome(), campos, null, null);
    }

    private void desenharDetalheHabilidade(TextGraphics graphics, TerminalSize tamanho) {
        List<Habilidade> habilidades = habilidadeService.listarHabilidades();
        if (habilidades.isEmpty()) { tela = Tela.HABILIDADES; return; }
        ajustarSelecao(habilidades.size());
        Habilidade habilidade = habilidades.get(selecao);
        List<String> campos = List.of(
            "Nível: " + habilidade.getNivel(), "Tipo: " + habilidade.getTipo(),
            "Alcance: " + habilidade.getAlcance(), "Recarga: " + habilidade.getRecarga(),
            "Custo: " + habilidade.getCusto()
        );
        desenharDetalhes(graphics, tamanho, "HABILIDADE", habilidade.getNome(), campos, "Descrição", habilidade.getDescricao());
    }

    private void desenharDetalheAnotacao(TextGraphics graphics, TerminalSize tamanho) {
        List<Anotacao> anotacoes = anotacaoService.listar();
        if (anotacoes.isEmpty()) { tela = Tela.ANOTACOES; return; }
        ajustarSelecao(anotacoes.size());
        Anotacao anotacao = anotacoes.get(selecao);
        desenharDetalhes(graphics, tamanho, "ANOTAÇÃO", anotacao.getTitulo(), List.of(), "Texto", anotacao.getTexto());
    }

    private void desenharCamposInline(TextGraphics graphics, TerminalSize tamanho, String[] campos, List<String> valores, int inicioY) {
        int largura = tamanho.getColumns();
        for (int i = 0; i < campos.length && inicioY + i < tamanho.getRows() - 2; i++) {
            escreverCampoInline(graphics, 3, inicioY + i, campos[i], valores.get(i), i == selecao, largura - 6);
        }
    }

    private void escreverCampoInline(
        TextGraphics graphics,
        int x,
        int y,
        String campo,
        String valor,
        boolean selecionado,
        int limite,
        SGR... estilos
    ) {
        String exibido = valor;
        int cursorExibido = 0;
        if (selecionado && editandoInline) {
            exibido = opcoesInline == null ? entradaInline.toString() : "‹ " + opcoesInline[opcaoInline] + " ›";
            int espacoValor = Math.max(1, limite - campo.length() - 4);
            int deslocamento = opcoesInline == null ? Math.max(0, cursorInline - espacoValor + 1) : 0;
            if (deslocamento > 0) exibido = exibido.substring(Math.min(deslocamento, exibido.length()));
            cursorExibido = opcoesInline == null ? cursorInline - deslocamento : exibido.length();
        }

        if (selecionado) {
            graphics.setForegroundColor(TextColor.ANSI.BLACK);
            graphics.setBackgroundColor(TextColor.ANSI.CYAN);
        }
        String prefixo = (selecionado ? "› " : "  ") + campo + ": ";
        escrever(graphics, x, y, prefixo + exibido, limite, estilos);
        if (selecionado && editandoInline) {
            cursorTelaX = Math.min(x + limite - 1, x + prefixo.length() + cursorExibido);
            cursorTelaY = y;
        }
        graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
    }

    private void desenharLista(TextGraphics graphics, TerminalSize tamanho, List<String> linhas, String vazia) {
        int largura = tamanho.getColumns();
        if (linhas.isEmpty()) {
            escrever(graphics, 3, 3, vazia, largura - 6);
            return;
        }
        ajustarSelecao(linhas.size());
        int disponiveis = Math.max(1, tamanho.getRows() - 6);
        int inicio = Math.max(0, selecao - disponiveis + 1);
        int fim = Math.min(linhas.size(), inicio + disponiveis);
        for (int i = inicio; i < fim; i++) {
            escreverSelecionado(graphics, 3, 2 + i - inicio, linhas.get(i), i == selecao, largura - 6);
        }
    }

    private void desenharDetalhes(
        TextGraphics graphics, TerminalSize tamanho, String titulo, String nome,
        List<String> campos, String tituloTexto, String texto
    ) {
        desenharMoldura(graphics, tamanho, titulo);
        int largura = tamanho.getColumns();
        escrever(graphics, 3, 2, nome, largura - 6, SGR.BOLD);
        desenharSeparador(graphics, 3, largura, "");
        int y = 5;
        for (String campo : campos) escrever(graphics, 3, y++, campo, largura - 6);
        if (tituloTexto != null) {
            y++;
            escrever(graphics, 3, y++, tituloTexto, largura - 6, SGR.BOLD);
            escreverQuebrado(graphics, 3, y, texto, largura - 6, tamanho.getRows() - y - 2);
        }
        escreverRodape(graphics, tamanho, "Esc Voltar   Q Sair");
    }

    private List<String> valoresDados(Ficha ficha) {
        return List.of(
            ficha.getNome(), String.valueOf(ficha.getIdade()), ficha.getSexo(),
            String.valueOf(ficha.getVidaMaxima()), String.valueOf(ficha.getVidaTemporaria()),
            String.valueOf(ficha.getVidaAtual()), ficha.getStatus(), ficha.getRaca(), ficha.getClasse(),
            String.valueOf(ficha.getNivel()), String.valueOf(ficha.getMoedaBronze()),
            String.valueOf(ficha.getMoedaPrata()), String.valueOf(ficha.getMoedaOuro())
        );
    }

    private List<String> valoresItem(Item item) {
        return List.of(
            item.getNome(), String.valueOf(item.getQuantidade()), item.getTipo(), item.getDescricao()
        );
    }

    private List<String> valoresEquipamento(Equipamento equipamento) {
        return List.of(
            equipamento.getNome(), equipamento.getTipo(), equipamento.getCategoria(), equipamento.getDano(),
            equipamento.getTipoDano(), equipamento.getPeso(), String.valueOf(equipamento.getValor()),
            equipamento.getRaridade()
        );
    }

    private List<String> valoresHabilidade(Habilidade habilidade) {
        return List.of(
            habilidade.getNome(), String.valueOf(habilidade.getNivel()), habilidade.getTipo(),
            habilidade.getAlcance(), habilidade.getRecarga(), habilidade.getCusto(), habilidade.getDescricao()
        );
    }

    private int[] valoresAtributos(Atributos atributos) {
        return new int[] {
            atributos.getBonusProficiencia(), atributos.getInteligencia(), atributos.getSabedoria(),
            atributos.getForca(), atributos.getDestreza(), atributos.getConstituicao(), atributos.getCarisma()
        };
    }

    private boolean campoDadoNumerico(int campo) {
        return campo == 1 || campo == 3 || campo == 4 || campo == 5 || campo >= 9;
    }

    private String rodapeEdicao(String padrao) {
        return aviso.isBlank() ? padrao : aviso;
    }

    private String vidaFormatada(Ficha ficha) {
        return "Vida: " + ficha.getVidaAtual() + " + " + ficha.getVidaTemporaria() + " (" + ficha.getVidaMaxima() + ")";
    }

    private String vazio(String valor, String padrao) {
        return valor == null || valor.isBlank() ? padrao : valor;
    }

    private void voltarAoPrincipal() {
        tela = Tela.PRINCIPAL;
        selecao = 0;
    }

    private void selecionarUltimo(int total) {
        selecao = Math.max(0, total - 1);
    }

    private void ajustarSelecao(int total) {
        selecao = total <= 0 ? 0 : Math.min(selecao, total - 1);
    }

    private boolean caractere(KeyStroke tecla, char esperado) {
        return tecla.getKeyType() == KeyType.Character && Character.toLowerCase(tecla.getCharacter()) == esperado;
    }

    private void desenharMoldura(TextGraphics graphics, TerminalSize tamanho, String titulo) {
        int largura = tamanho.getColumns();
        int altura = tamanho.getRows();
        if (largura < 2 || altura < 2) return;
        String horizontal = "─".repeat(Math.max(0, largura - 2));
        escrever(graphics, 0, 0, "┌" + horizontal + "┐", largura);
        escrever(graphics, 0, altura - 1, "└" + horizontal + "┘", largura);
        for (int y = 1; y < altura - 1; y++) {
            escrever(graphics, 0, y, "│", 1);
            escrever(graphics, largura - 1, y, "│", 1);
        }
        escreverCentralizado(graphics, 0, " " + titulo + " ", largura, SGR.BOLD);
    }

    private void desenharSeparador(TextGraphics graphics, int y, int largura, String titulo) {
        if (y < 1) return;
        escrever(graphics, 0, y, "├" + "─".repeat(Math.max(0, largura - 2)) + "┤", largura);
        if (!titulo.isEmpty()) escreverCentralizado(graphics, y, " " + titulo + " ", largura, SGR.BOLD);
    }

    private void escreverRodape(TextGraphics graphics, TerminalSize tamanho, String texto) {
        escreverCentralizado(graphics, tamanho.getRows() - 2, texto, tamanho.getColumns());
    }

    private void escreverSelecionado(
        TextGraphics graphics, int x, int y, String texto, boolean selecionado, int limite, SGR... estilos
    ) {
        if (selecionado) {
            graphics.setForegroundColor(TextColor.ANSI.BLACK);
            graphics.setBackgroundColor(TextColor.ANSI.CYAN);
        }
        escrever(graphics, x, y, (selecionado ? "› " : "  ") + texto, limite, estilos);
        graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
    }

    private void escreverDireita(TextGraphics graphics, int y, String texto, int largura) {
        int x = Math.max(2, largura - texto.length() - 3);
        escrever(graphics, x, y, texto, Math.max(0, largura - x - 1));
    }

    private void escreverCentralizado(TextGraphics graphics, int y, String texto, int largura, SGR... estilos) {
        int x = Math.max(0, (largura - texto.length()) / 2);
        escrever(graphics, x, y, texto, largura - x, estilos);
    }

    private void escrever(TextGraphics graphics, int x, int y, String texto, int limite, SGR... estilos) {
        if (x < 0 || y < 0 || limite <= 0 || texto == null) return;
        String exibido = texto.length() > limite ? texto.substring(0, limite) : texto;
        for (SGR estilo : estilos) graphics.enableModifiers(estilo);
        graphics.putString(x, y, exibido);
        for (SGR estilo : estilos) graphics.disableModifiers(estilo);
    }

    private void escreverQuebrado(TextGraphics graphics, int x, int y, String texto, int largura, int maximoLinhas) {
        if (largura <= 0 || maximoLinhas <= 0 || texto == null) return;
        String restante = texto;
        for (int linha = 0; linha < maximoLinhas && !restante.isEmpty(); linha++) {
            int corte = Math.min(largura, restante.length());
            if (corte < restante.length()) {
                int espaco = restante.lastIndexOf(' ', corte);
                if (espaco > 0) corte = espaco;
            }
            escrever(graphics, x, y + linha, restante.substring(0, corte).trim(), largura);
            restante = restante.substring(corte).trim();
        }
    }

    private int circular(int valor, int total) {
        return (valor % total + total) % total;
    }

    private enum Tela {
        PRINCIPAL, DADOS, ATRIBUTOS, ITENS, EDICAO_ITEM, INVENTARIO, DETALHE_ITEM,
        EQUIPAMENTOS, EDICAO_EQUIPAMENTO, PERMISSOES_EQUIPAMENTO, DETALHE_EQUIPAMENTO,
        HABILIDADES, EDICAO_HABILIDADE, DETALHE_HABILIDADE,
        ANOTACOES, EDICAO_ANOTACAO, DETALHE_ANOTACAO
    }

    @FunctionalInterface
    private interface AcaoEdicao {
        void executar(String valor);
    }
}
