package br.com.ficha;

import java.util.Scanner;
import java.util.List;

import br.com.ficha.controller.HabilidadesController;
import br.com.ficha.controller.ItensController;
import br.com.ficha.model.Ficha;
import br.com.ficha.model.Equipamento;
import br.com.ficha.repository.EquipamentoRepository;
import br.com.ficha.repository.AnotacaoRepository;
import br.com.ficha.model.Anotacao;
import br.com.ficha.repository.FichaRepository;
import br.com.ficha.repository.HabilidadeRepository;
import br.com.ficha.repository.ItemRepository;
import br.com.ficha.service.FichaService;
import br.com.ficha.service.HabilidadeService;
import br.com.ficha.service.ItemService;
import br.com.ficha.ui.MenuHabilidades;
import br.com.ficha.ui.MenuItens;

public class App {
    private static final int TERMINAL_ROWS = 20;
    private static final int TERMINAL_COLUMNS = 72;
    private static final Scanner scanner = new Scanner(System.in);
    private static final FichaService fichaService = new FichaService(new FichaRepository());

    public static void main(String[] args) {
        ajustarTamanhoTerminal();
        iniciar(scanner);

        System.out.println("Saindo do sistema. Ate mais!");
        scanner.close();
    }

    public static void iniciar(Scanner scanner) {
        boolean continuar = true;
        clear();

        while (continuar) {
            Ficha ficha = fichaService.carregarFicha();
            clear();
            cabecalho();
            menuDadosPersonagem(ficha);
            menuInterativo();

            System.out.println("Selecione uma opção:");
            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    clear();
                    menuEditarDadosPersonagem(scanner);
                    break;
                case 2:
                    clear();
                    abrirMenuAtributos(scanner);
                    break;
                case 3:
                    abrirMenuItens(scanner);
                    break;
                case 4:
                    abrirMenuEquipamentos(scanner);
                    break;
                case 5:
                    clear();
                    abrirMenuHabilidades(scanner);
                    break;
                case 6:
                    clear();
                    abrirMenuAnotacoes(scanner);
                    break;
                case 0:
                    continuar = false;
                    System.out.println("Saindo...");
                    clear();
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void cabecalho() {
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
        System.out.println("            Bem-vindo ao sistema de ficha");
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    private static int lerOpcao(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine();
            if (entrada.equals("\u001B")) {
                return Integer.MIN_VALUE;
            }
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, insira um numero.");
            }
        }
    }

    public static void menuDadosPersonagem(Ficha ficha) {
        System.out.println("DADOS PERSONAGEM");
        System.out.println();
        System.out.println("Nome: " + ficha.getNome());
        System.out.println("Idade: " + ficha.getIdade());
        System.out.println("Sexo: " + ficha.getSexo());
        System.out.println("Vida: " + ficha.getVida());
        System.out.println("Status: " + ficha.getStatus());
        System.out.println("Raça: " + ficha.getRaca());
        System.out.println("Classe: " + ficha.getClasse());
        System.out.println("Nível: " + ficha.getNivel());
        exibirTabelaMoedas(ficha);

        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    private static void exibirTabelaMoedas(Ficha ficha) {
        System.out.println();
        System.out.println("+---------------------+");
        System.out.println("|       MOEDAS        |");
        System.out.println("+------------+--------+");
        System.out.printf("| %-10s | %-6d |%n", "Bronze", ficha.getMoedaBronze());
        System.out.printf("| %-10s | %-6d |%n", "Prata", ficha.getMoedaPrata());
        System.out.printf("| %-10s | %-6d |%n", "Ouro", ficha.getMoedaOuro());
        System.out.println("+------------+--------+");
    }

    public static void menuInterativo() {
        System.out.println("1 - Editar dados personagem");
        System.out.println("2 - Atributos");
        System.out.println("3 - Itens");
        System.out.println("4 - Equipamentos");
        System.out.println("5 - Habilidades");
        System.out.println("6 - Anotações");
        System.out.println("0 - Sair");
    }

    public static void menuEditarDadosPersonagem(Scanner scanner) {
        boolean noMenu = true;
        Ficha ficha = fichaService.carregarFicha();

        while (noMenu) {
            clear();
            cabecalho();
            menuDadosPersonagem(ficha);
            System.out.println("Editar Dados Personagem");
            System.out.println();
            System.out.println("1 - Editar nome");
            System.out.println("2 - Editar idade");
            System.out.println("3 - Editar sexo");
            System.out.println("4 - Editar vida");
            System.out.println("5 - Editar status");
            System.out.println("6 - Editar raca");
            System.out.println("7 - Editar classe");
            System.out.println("8 - Editar nivel");
            System.out.println("9 - Editar moeda de bronze");
            System.out.println("10 - Editar moeda de prata");
            System.out.println("11 - Editar moeda de ouro");
            System.out.println("0 - Voltar");

            int opcao = lerOpcao(scanner);
            if (opcao == 0) {
                noMenu = false;
                clear();
            } else {
                editarAtributo(scanner, ficha, opcao);
                fichaService.salvarFicha(ficha);
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void editarAtributo(Scanner scanner, Ficha ficha, int opcao) {
        switch (opcao) {
            case 1:
                System.out.println("Digite o nome:");
                ficha.setNome(scanner.nextLine().trim());
                System.out.println("Nome atualizado com sucesso.");
                break;
            case 2:
                System.out.println("Digite a idade:");
                ficha.setIdade(lerInteiroPositivo(scanner));
                System.out.println("Idade atualizada com sucesso.");
                break;
            case 3:
                System.out.println("Digite o sexo:");
                ficha.setSexo(scanner.nextLine().trim());
                System.out.println("Sexo atualizado com sucesso.");
                break;
            case 4:
                System.out.println("Digite a vida:");
                ficha.setVida(lerInteiroPositivo(scanner));
                System.out.println("Vida atualizada com sucesso.");
                break;
            case 5:
                System.out.println("Digite o status:");
                ficha.setStatus(scanner.nextLine().trim());
                System.out.println("Status atualizado com sucesso.");
                break;
            case 6:
                System.out.println("Digite a raca:");
                ficha.setRaca(scanner.nextLine().trim());
                System.out.println("Raca atualizada com sucesso.");
                break;
            case 7:
                System.out.println("Digite a classe:");
                ficha.setClasse(scanner.nextLine().trim());
                System.out.println("Classe atualizada com sucesso.");
                break;
            case 8:
                System.out.println("Digite o nivel:");
                ficha.setNivel(lerInteiroPositivo(scanner));
                System.out.println("Nivel atualizado com sucesso.");
                break;
            case 9:
                System.out.println("Digite a quantidade de moedas de bronze:");
                ficha.setMoedaBronze(lerInteiroPositivo(scanner));
                System.out.println("Moedas de bronze atualizadas com sucesso.");
                break;
            case 10:
                System.out.println("Digite a quantidade de moedas de prata:");
                ficha.setMoedaPrata(lerInteiroPositivo(scanner));
                System.out.println("Moedas de prata atualizadas com sucesso.");
                break;
            case 11:
                System.out.println("Digite a quantidade de moedas de ouro:");
                ficha.setMoedaOuro(lerInteiroPositivo(scanner));
                System.out.println("Moedas de ouro atualizadas com sucesso.");
                break;
            default:
                System.out.println("Opcao invalida.");
        }
    }

    private static int lerInteiroPositivo(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine();
            try {
                int valor = Integer.parseInt(entrada.trim());
                if (valor >= 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // continua no loop
            }
            System.out.println("Entrada invalida. Digite um numero inteiro maior ou igual a zero.");
        }
    }

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[3J\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Nao foi possivel limpar a tela!");
        }
    }

    private static void ajustarTamanhoTerminal() {
        if (System.console() == null) {
            return;
        }

        // ANSI CSI 8 resizes the terminal window in terminals that support it.
        System.out.printf("\033[8;%d;%dt", TERMINAL_ROWS, TERMINAL_COLUMNS);
        System.out.flush();
    }

    private static void abrirMenuHabilidades(Scanner scanner) {
        HabilidadeRepository repository = new HabilidadeRepository();
        HabilidadeService service = new HabilidadeService(repository);
        MenuHabilidades view = new MenuHabilidades(scanner);
        HabilidadesController controller = new HabilidadesController(service, view, App::clear, App::cabecalho);
        controller.iniciar();
    }

    private static void abrirMenuAtributos(Scanner scanner) {
        boolean noMenu = true;
        Ficha ficha = fichaService.carregarFicha();

        while (noMenu) {
            clear();
            cabecalho();
            exibirAtributos(ficha);
            System.out.println("Editar Atributos");
            System.out.println();
            System.out.println("1 - Editar Inteligência");
            System.out.println("2 - Editar sabedoria");
            System.out.println("3 - Editar Força");
            System.out.println("4 - Editar destreza");
            System.out.println("5 - Editar Constituição");
            System.out.println("6 - Editar carisma");
            System.out.println("7 - Editar Bônus de Proficiência");
            System.out.println("0 - Voltar");

            int opcao = lerOpcao(scanner);
            if (opcao == 0) {
                noMenu = false;
                clear();
            } else {
                editarAtributoPersonagem(scanner, ficha, opcao);
                fichaService.salvarFicha(ficha);
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void exibirAtributos(Ficha ficha) {
        System.out.println("ATRIBUTOS");
        System.out.println();
        System.out.println("+----------------------------------+");
        System.out.printf("| BÔNUS DE PROFICIÊNCIA: %-8d |%n", ficha.getBonusProficiencia());
        System.out.println("+----------------------------------+");
        System.out.println();
        System.out.println("Inteligência: " + ficha.getInteligencia());
        System.out.println("Sabedoria: " + ficha.getSabedoria());
        System.out.println("Força: " + ficha.getForca());
        System.out.println("Destreza: " + ficha.getDestreza());
        System.out.println("Constituição: " + ficha.getConstituicao());
        System.out.println("Carisma: " + ficha.getCarisma());
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    private static void editarAtributoPersonagem(Scanner scanner, Ficha ficha, int opcao) {
        if (opcao < 1 || opcao > 7) {
            System.out.println("Opcao invalida.");
            return;
        }

        System.out.println("Digite um numero inteiro positivo:");
        int valor = lerInteiroPositivoEstrito(scanner);

        switch (opcao) {
            case 1:
                ficha.setInteligencia(valor);
                break;
            case 2:
                ficha.setSabedoria(valor);
                break;
            case 3:
                ficha.setForca(valor);
                break;
            case 4:
                ficha.setDestreza(valor);
                break;
            case 5:
                ficha.setConstituicao(valor);
                break;
            case 6:
                ficha.setCarisma(valor);
                break;
            case 7:
                ficha.setBonusProficiencia(valor);
                break;
            default:
                return;
        }

        System.out.println("Atributo atualizado com sucesso.");
    }

    private static int lerInteiroPositivoEstrito(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine();
            try {
                int valor = Integer.parseInt(entrada.trim());
                if (valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // continua no loop
            }
            System.out.println("Entrada invalida. Digite um numero inteiro positivo.");
        }
    }

    private static void abrirMenuItens(Scanner scanner) {
        ItemRepository repository = new ItemRepository();
        ItemService service = new ItemService(repository);
        MenuItens view = new MenuItens(scanner);
        ItensController controller = new ItensController(service, view, App::clear, App::cabecalho);
        controller.iniciar();
    }

    private static void abrirMenuEquipamentos(Scanner scanner) {
        EquipamentoRepository repository = new EquipamentoRepository();
        boolean aberto = true;
        while (aberto) {
            clear(); cabecalho();
            System.out.println("EQUIPAMENTOS");
            System.out.println("1 - Exibir equipamentos");
            System.out.println("2 - Adicionar equipamento");
            System.out.println("3 - Equipar ou desequipar");
            System.out.println("4 - Configurar permissoes de armadura");
            System.out.println("5 - Editar equipamento");
            System.out.println("0 - Voltar");
            switch (lerOpcao(scanner)) {
                case 1: exibirEquipamentos(scanner, repository); break;
                case 2: adicionarEquipamento(scanner, repository); break;
                case 3: alterarEquipamento(scanner, repository); break;
                case 4: configurarPermissoes(scanner, repository); break;
                case 5: editarEquipamento(scanner, repository); break;
                case 0: aberto = false; clear(); break;
                default: System.out.println("Opcao invalida."); scanner.nextLine();
            }
        }
    }

    private static void exibirEquipamentos(Scanner scanner, EquipamentoRepository repository) {
        clear(); cabecalho();
        List<Equipamento> lista = repository.listar();
        if (lista.isEmpty()) System.out.println("Nenhum equipamento encontrado.");
        for (int i = 0; i < lista.size(); i++) {
            Equipamento e = lista.get(i);
            System.out.println((i + 1) + " - " + e.getNome() + (e.getEquipadoComo().isEmpty() ? "" : " [" + e.getEquipadoComo() + "]"));
        }
        if (!lista.isEmpty()) {
            System.out.println("\nDigite o numero para ver detalhes ou 0 para voltar:");
            int opcao = lerOpcao(scanner);
            if (opcao > 0 && opcao <= lista.size()) exibirDetalhesEquipamento(lista.get(opcao - 1));
        }
        System.out.println("\nPressione Enter para continuar..."); scanner.nextLine();
    }

    private static void exibirDetalhesEquipamento(Equipamento e) {
        System.out.println("\n" + e.getNome());
        System.out.println("\nTipo: " + e.getTipo());
        System.out.println("Categoria: " + e.getCategoria());
        System.out.println("Dano: " + e.getDano());
        System.out.println("Tipo de Dano: " + e.getTipoDano());
        System.out.println("Peso: " + e.getPeso() + " kg");
        System.out.println("Valor: " + e.getValor() + " moedas");
        System.out.println("Raridade: " + e.getRaridade());
        System.out.println("Equipado: " + (e.getEquipadoComo().isEmpty() ? "Nao" : "Sim (" + e.getEquipadoComo() + ")"));
    }

    private static void adicionarEquipamento(Scanner scanner, EquipamentoRepository repository) {
        clear(); cabecalho(); System.out.println("ADICIONAR EQUIPAMENTO");
        System.out.println("Nome (Esc para cancelar):"); String nome = lerTexto(scanner); if (nome == null) return;
        System.out.println("Tipo: 1 - Arma | 2 - Armadura | 3 - Outro | Esc - Cancelar");
        int tipoOpcao = lerOpcaoAdicionar(scanner, 3); if (tipoOpcao == 0) return; String tipo = tipoOpcao == 1 ? "Arma" : tipoOpcao == 2 ? "Armadura" : "Outro";
        String categoria;
        if (tipoOpcao == 2) {
            System.out.println("Categoria: 1 - Leve | 2 - Media | 3 - Pesada | Esc - Cancelar");
            int categoriaOpcao = lerOpcaoAdicionar(scanner, 3); if (categoriaOpcao == 0) return; categoria = categoriaOpcao == 1 ? "Leve" : categoriaOpcao == 2 ? "Media" : "Pesada";
        } else { System.out.println("Categoria (0 para cancelar):"); categoria = lerTexto(scanner); if (categoria == null) return; }
        System.out.println("Dano (use N/A se nao se aplicar, Esc para cancelar):"); String dano = lerTexto(scanner); if (dano == null) return;
        System.out.println("Tipo de dano (use N/A se nao se aplicar, Esc para cancelar):"); String tipoDano = lerTexto(scanner); if (tipoDano == null) return;
        System.out.println("Peso em kg (Esc para cancelar):"); String peso = lerTexto(scanner); if (peso == null) return;
        System.out.println("Valor em moedas (Esc para cancelar):"); int valor = lerInteiroPositivoOuCancelar(scanner); if (valor == 0) return;
        System.out.println("Raridade: 1 - Comum | 2 - Incomum | 3 - Raro | 4 - Muito Raro | 5 - Lendario | Esc - Cancelar");
        String[] raridades = {"Comum", "Incomum", "Raro", "Muito Raro", "Lendario"};
        int raridadeOpcao = lerOpcaoAdicionar(scanner, 5); if (raridadeOpcao == 0) return; String raridade = raridades[raridadeOpcao - 1];
        List<Equipamento> lista = repository.listar(); lista.add(new Equipamento(nome, tipo, categoria, dano, tipoDano, peso, valor, raridade, "")); repository.salvar(lista);
        System.out.println("Equipamento adicionado com sucesso.\nPressione Enter para continuar..."); scanner.nextLine();
    }

    private static void alterarEquipamento(Scanner scanner, EquipamentoRepository repository) {
        clear(); cabecalho(); List<Equipamento> lista = repository.listar();
        if (lista.isEmpty()) { System.out.println("Nenhum equipamento encontrado."); scanner.nextLine(); return; }
        for (int i = 0; i < lista.size(); i++) System.out.println((i + 1) + " - " + lista.get(i).getNome());
        System.out.println("Escolha o equipamento ou 0 para voltar:"); int opcao = lerOpcao(scanner);
        if (opcao < 1 || opcao > lista.size()) return;
        Equipamento escolhido = lista.get(opcao - 1);
        if (!escolhido.getEquipadoComo().isEmpty()) { escolhido.setEquipadoComo(""); repository.salvar(lista); System.out.println("Equipamento desequipado."); scanner.nextLine(); return; }
        String slot;
        if (escolhido.getTipo().equals("Arma")) {
            System.out.println("1 - Principal | 2 - Secundaria"); slot = lerOpcaoIntervalo(scanner, 1, 2) == 1 ? "Principal" : "Secundaria";
        } else if (escolhido.getTipo().equals("Armadura")) {
            boolean[] p = repository.carregarPermissoes(); int indice = escolhido.getCategoria().equals("Leve") ? 0 : escolhido.getCategoria().equals("Media") ? 1 : 2;
            if (!p[indice]) { System.out.println("Esta categoria de armadura nao esta permitida."); scanner.nextLine(); return; } slot = "Armadura";
        } else { System.out.println("Apenas armas e armaduras podem ser equipadas."); scanner.nextLine(); return; }
        for (Equipamento e : lista) if (e.getEquipadoComo().equals(slot)) { System.out.println("Ja existe equipamento no slot " + slot + ". Desequipe-o primeiro."); scanner.nextLine(); return; }
        escolhido.setEquipadoComo(slot); repository.salvar(lista); System.out.println("Equipamento equipado como " + slot + "."); scanner.nextLine();
    }

    private static void configurarPermissoes(Scanner scanner, EquipamentoRepository repository) {
        boolean[] p = repository.carregarPermissoes();
        for (int i = 0; i < 3; i++) { String categoria = i == 0 ? "Leve" : i == 1 ? "Media" : "Pesada"; System.out.println("Permitir armadura " + categoria + "? (1 - Sim | 2 - Nao)"); p[i] = lerOpcaoIntervalo(scanner, 1, 2) == 1; }
        repository.salvarPermissoes(p); System.out.println("Permissoes atualizadas.\nPressione Enter para continuar..."); scanner.nextLine();
    }

    private static void editarEquipamento(Scanner scanner, EquipamentoRepository repository) {
        clear(); cabecalho(); List<Equipamento> lista = repository.listar();
        for (int i = 0; i < lista.size(); i++) System.out.println((i + 1) + " - " + lista.get(i).getNome());
        if (lista.isEmpty()) { System.out.println("Nenhum equipamento encontrado."); scanner.nextLine(); return; }
        System.out.println("Escolha o equipamento ou 0 para voltar:"); int indice = lerOpcao(scanner);
        if (indice < 1 || indice > lista.size()) return;
        Equipamento e = lista.get(indice - 1);
        exibirDetalhesEquipamento(e);
        System.out.println("Campo: 1-Nome 2-Categoria 3-Dano 4-Tipo de dano 5-Peso 6-Valor 7-Raridade");
        int campo = lerOpcaoIntervalo(scanner, 1, 7);
        String antigo = campo == 1 ? e.getNome() : campo == 2 ? e.getCategoria() : campo == 3 ? e.getDano() : campo == 4 ? e.getTipoDano() : campo == 5 ? e.getPeso() : campo == 6 ? String.valueOf(e.getValor()) : e.getRaridade();
        if (campo == 6) { System.out.println("Valor atual: " + antigo + "\nNovo valor:"); e.setValor(lerInteiroPositivoEstrito(scanner)); }
        else { System.out.println("Valor atual: " + antigo + "\nNovo valor:"); String valor = lerTexto(scanner); if (valor == null) return;
            if (campo == 1) e.setNome(valor); else if (campo == 2) e.setCategoria(valor); else if (campo == 3) e.setDano(valor); else if (campo == 4) e.setTipoDano(valor); else if (campo == 5) e.setPeso(valor); else e.setRaridade(valor); }
        repository.salvar(lista); System.out.println("Equipamento atualizado com sucesso."); scanner.nextLine();
    }

    private static String lerTexto(Scanner scanner) { String texto = scanner.nextLine(); if (texto.equals("\u001B")) return null; texto = texto.trim(); while (texto.isEmpty()) { System.out.println("Entrada invalida. Digite um texto."); texto = scanner.nextLine(); if (texto.equals("\u001B")) return null; texto = texto.trim(); } return texto; }
    private static int lerOpcaoAdicionar(Scanner scanner, int maximo) { int opcao; do { opcao = lerOpcao(scanner); if (opcao == Integer.MIN_VALUE) return 0; if (opcao < 1 || opcao > maximo) System.out.println("Opcao invalida."); } while (opcao < 1 || opcao > maximo); return opcao; }
    private static int lerInteiroPositivoOuCancelar(Scanner scanner) { int valor; do { valor = lerOpcao(scanner); if (valor == Integer.MIN_VALUE) return 0; if (valor <= 0) System.out.println("Entrada invalida."); } while (valor <= 0); return valor; }
    private static int lerOpcaoIntervalo(Scanner scanner, int minimo, int maximo) { int opcao; do { opcao = lerOpcao(scanner); if (opcao < minimo || opcao > maximo) System.out.println("Opcao invalida."); } while (opcao < minimo || opcao > maximo); return opcao; }

    private static void abrirMenuAnotacoes(Scanner scanner) {
        AnotacaoRepository r = new AnotacaoRepository();
        boolean aberto = true;
        while (aberto) {
            clear(); cabecalho();
            List<Anotacao> a = r.listar();
            System.out.println("ANOTAÇÕES\n1 - Exibir anotações\n2 - Adicionar anotação\n3 - Excluir anotação\n4 - Editar anotação\n0 - Voltar");
            int o = lerOpcao(scanner);
            if (o == 0) { aberto = false; continue; }
            if (o == 1) {
                if (a.isEmpty()) System.out.println("Nenhuma anotação encontrada.");
                for (int i = 0; i < a.size(); i++) System.out.println((i + 1) + " - " + a.get(i).getTitulo());
                if (!a.isEmpty()) {
                    System.out.println("Número ou 0:"); int n = lerOpcao(scanner);
                    if (n > 0 && n <= a.size()) System.out.println("\nTítulo: " + a.get(n - 1).getTitulo() + "\nTexto: " + a.get(n - 1).getTexto());
                }
                System.out.println("\nPressione Enter para continuar..."); scanner.nextLine(); continue;
            }
            if (o < 2 || o > 4) continue;
            if (o == 2) { System.out.println("Título:"); String t = lerTexto(scanner); if (t == null) continue; System.out.println("Texto:"); String x = lerTexto(scanner); if (x == null) continue; a.add(new Anotacao(t, x)); }
            else {
                if (a.isEmpty()) {
                    System.out.println("Nenhuma anotação encontrada.");
                    System.out.println("Pressione Enter para continuar..."); scanner.nextLine(); continue;
                }
                for (int i = 0; i < a.size(); i++) System.out.println((i + 1) + " - " + a.get(i).getTitulo());
                System.out.println(o == 3 ? "Número da anotação para excluir ou 0:" : "Número da anotação para editar ou 0:");
                int n = lerOpcao(scanner); if (n < 1 || n > a.size()) continue;
                if (o == 4) {
                    System.out.println("Título atual: " + a.get(n - 1).getTitulo() + "\nNovo título:"); String t = lerTexto(scanner); if (t == null) continue;
                    System.out.println("Texto atual: " + a.get(n - 1).getTexto() + "\nNovo texto:"); String x = lerTexto(scanner); if (x == null) continue;
                    a.set(n - 1, new Anotacao(t, x)); System.out.println("Anotação atualizada com sucesso.");
                } else { a.remove(n - 1); System.out.println("Anotação excluída com sucesso."); }
            }
            r.salvar(a); System.out.println("Pressione Enter para continuar..."); scanner.nextLine();
        }
    }
}
