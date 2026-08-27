package br.com.ficha.controller;

import br.com.ficha.service.ItemService;
import br.com.ficha.ui.MenuInventario;

public class InventarioController {
    private final ItemService itemService;
    private final MenuInventario menu;
    private final Runnable limparTela;
    private final Runnable exibirCabecalho;

    public InventarioController(
        ItemService itemService,
        MenuInventario menu,
        Runnable limparTela,
        Runnable exibirCabecalho
    ) {
        this.itemService = itemService;
        this.menu = menu;
        this.limparTela = limparTela;
        this.exibirCabecalho = exibirCabecalho;
    }

    public void iniciar() {
        limparTela.run();
        exibirCabecalho.run();
        menu.exibir(itemService.listarItens());
        menu.aguardarEnter();
        limparTela.run();
    }
}
