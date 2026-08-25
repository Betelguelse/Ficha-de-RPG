package br.com.ficha.controller;

import java.util.List;

import br.com.ficha.model.Item;
import br.com.ficha.service.ItemService;
import br.com.ficha.ui.MenuItens;

public class ItensController {
    private final ItemService itemService;
    private final MenuItens menuItens;
    private final Runnable limparTela;
    private final Runnable exibirCabecalho;

    public ItensController(ItemService itemService, MenuItens menuItens, Runnable limparTela, Runnable exibirCabecalho) {
        this.itemService = itemService;
        this.menuItens = menuItens;
        this.limparTela = limparTela;
        this.exibirCabecalho = exibirCabecalho;
    }

    public void iniciar() {
        boolean executando = true;

        while (executando) {
            limparTela.run();
            exibirCabecalho.run();
            menuItens.exibirMenu();

            switch (menuItens.lerOpcao()) {
                case 1:
                    exibirItens();
                    break;
                case 2:
                    adicionarItem();
                    break;
                case 3:
                    excluirItem();
                    break;
                case 4:
                    editarItem();
                    break;
                case 0:
                    limparTela.run();
                    executando = false;
                    break;
                default:
                    menuItens.exibirMensagem("Opcao invalida.");
                    aguardarContinuacao();
            }
        }
    }

    private void exibirItens() {
        limparTela.run();
        exibirCabecalho.run();
        List<Item> itens = itemService.listarItens();
        menuItens.exibirLista(itens);

        if (!itens.isEmpty()) {
            int opcao = menuItens.lerNumeroItem("\nEscolha um item para ver detalhes ou 0 para voltar:");
            if (opcao == 0) {
                return;
            }
            try {
                menuItens.exibirDetalhes(itemService.buscarPorIndice(opcao - 1));
            } catch (IllegalArgumentException e) {
                menuItens.exibirMensagem(e.getMessage());
            }
            aguardarContinuacao();
        } else {
            aguardarContinuacao();
        }
    }

    private void adicionarItem() {
        limparTela.run();
        exibirCabecalho.run();
        Item item = menuItens.lerNovoItem();
        if (item == null) {
            menuItens.exibirMensagem("Adicao cancelada.");
            aguardarContinuacao();
            return;
        }
        itemService.adicionarItem(item);
        menuItens.exibirMensagem("Item adicionado com sucesso!");
        aguardarContinuacao();
    }

    private void excluirItem() {
        limparTela.run();
        exibirCabecalho.run();
        List<Item> itens = itemService.listarItens();
        menuItens.exibirLista(itens);
        if (itens.isEmpty()) {
            aguardarContinuacao();
            return;
        }

        int opcao = menuItens.lerNumeroItem("\nDigite o numero do item que deseja excluir ou 0 para cancelar:");
        if (opcao == 0) {
            return;
        }

        try {
            Item removido = itemService.excluirItem(opcao - 1);
            menuItens.exibirMensagem("Item excluido com sucesso: " + removido.getNome());
        } catch (IllegalArgumentException e) {
            menuItens.exibirMensagem(e.getMessage());
        }
        aguardarContinuacao();
    }

    private void editarItem() {
        limparTela.run(); exibirCabecalho.run();
        List<Item> itens = itemService.listarItens(); menuItens.exibirLista(itens);
        if (itens.isEmpty()) { aguardarContinuacao(); return; }
        int opcao = menuItens.lerNumeroItem("\nDigite o numero do item para editar ou 0 para cancelar:");
        if (opcao == 0) return;
        try {
            menuItens.exibirDetalhes(itemService.buscarPorIndice(opcao - 1));
            Item atualizado = menuItens.lerNovoItem();
            if (atualizado == null) { menuItens.exibirMensagem("Edicao cancelada."); }
            else { itemService.editarItem(opcao - 1, atualizado); menuItens.exibirMensagem("Item atualizado com sucesso!"); }
        } catch (IllegalArgumentException e) { menuItens.exibirMensagem(e.getMessage()); }
        aguardarContinuacao();
    }

    private void aguardarContinuacao() {
        menuItens.exibirMensagem("Pressione Enter para continuar...");
        menuItens.aguardarEnter();
    }
}
