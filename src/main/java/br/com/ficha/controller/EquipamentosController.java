package br.com.ficha.controller;

import java.util.List;

import br.com.ficha.model.Equipamento;
import br.com.ficha.service.EquipamentoService;
import br.com.ficha.ui.MenuEquipamentos;

public class EquipamentosController {
    private final EquipamentoService service;
    private final MenuEquipamentos menu;
    private final Runnable limparTela;
    private final Runnable exibirCabecalho;

    public EquipamentosController(
        EquipamentoService service,
        MenuEquipamentos menu,
        Runnable limparTela,
        Runnable exibirCabecalho
    ) {
        this.service = service;
        this.menu = menu;
        this.limparTela = limparTela;
        this.exibirCabecalho = exibirCabecalho;
    }

    public void iniciar() {
        boolean executando = true;

        while (executando) {
            prepararTela();
            menu.exibirMenu();

            switch (menu.lerOpcao()) {
                case 1 -> exibirEquipamentos();
                case 2 -> adicionarEquipamento();
                case 3 -> alterarEquipamento();
                case 4 -> configurarPermissoes();
                case 5 -> editarEquipamento();
                case 0 -> {
                    limparTela.run();
                    executando = false;
                }
                default -> {
                    menu.exibirMensagem("Opção inválida.");
                    aguardarContinuacao();
                }
            }
        }
    }

    private void exibirEquipamentos() {
        prepararTela();
        List<Equipamento> equipamentos = service.listar();
        menu.exibirLista(equipamentos);

        if (!equipamentos.isEmpty()) {
            int opcao = menu.lerIndice("\nDigite o número para ver detalhes ou 0 para voltar:");
            if (opcao > 0) {
                try {
                    menu.exibirDetalhes(service.buscarPorIndice(opcao - 1));
                } catch (IllegalArgumentException e) {
                    menu.exibirMensagem(e.getMessage());
                }
            }
        }
        aguardarContinuacao();
    }

    private void adicionarEquipamento() {
        prepararTela();
        Equipamento equipamento = menu.lerNovoEquipamento();
        if (equipamento == null) {
            menu.exibirMensagem("Adição cancelada.");
        } else {
            service.adicionar(equipamento);
            menu.exibirMensagem("Equipamento adicionado com sucesso.");
        }
        aguardarContinuacao();
    }

    private void alterarEquipamento() {
        prepararTela();
        List<Equipamento> equipamentos = service.listar();
        menu.exibirLista(equipamentos);
        if (equipamentos.isEmpty()) {
            aguardarContinuacao();
            return;
        }

        int opcao = menu.lerIndice("Escolha o equipamento ou 0 para voltar:");
        if (opcao == 0) return;

        try {
            Equipamento equipamento = service.buscarPorIndice(opcao - 1);
            String slot = equipamento.getTipo().equals("Arma") && equipamento.getEquipadoComo().isEmpty()
                ? menu.lerSlotArma()
                : null;
            menu.exibirMensagem(service.alternarEquipado(opcao - 1, slot));
        } catch (IllegalArgumentException e) {
            menu.exibirMensagem(e.getMessage());
        }
        aguardarContinuacao();
    }

    private void configurarPermissoes() {
        prepararTela();
        boolean[] permissoes = menu.lerPermissoes(service.carregarPermissoes());
        service.salvarPermissoes(permissoes);
        menu.exibirMensagem("Permissões atualizadas.");
        aguardarContinuacao();
    }

    private void editarEquipamento() {
        prepararTela();
        List<Equipamento> equipamentos = service.listar();
        menu.exibirLista(equipamentos);
        if (equipamentos.isEmpty()) {
            aguardarContinuacao();
            return;
        }

        int opcao = menu.lerIndice("Escolha o equipamento ou 0 para voltar:");
        if (opcao == 0) return;

        try {
            Equipamento atual = service.buscarPorIndice(opcao - 1);
            Equipamento editado = menu.lerEdicao(atual);
            if (editado == null) {
                menu.exibirMensagem("Edição cancelada.");
            } else {
                service.editar(opcao - 1, editado);
                menu.exibirMensagem("Equipamento atualizado com sucesso.");
            }
        } catch (IllegalArgumentException e) {
            menu.exibirMensagem(e.getMessage());
        }
        aguardarContinuacao();
    }

    private void prepararTela() {
        limparTela.run();
        exibirCabecalho.run();
    }

    private void aguardarContinuacao() {
        menu.exibirMensagem("Pressione Enter para continuar...");
        menu.aguardarEnter();
    }
}
