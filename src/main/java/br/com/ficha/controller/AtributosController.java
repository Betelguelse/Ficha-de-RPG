package br.com.ficha.controller;

import br.com.ficha.model.Atributos;
import br.com.ficha.model.Ficha;
import br.com.ficha.service.FichaService;
import br.com.ficha.ui.MenuAtributos;

public class AtributosController {
    private final FichaService service;
    private final MenuAtributos menu;
    private final Runnable limparTela;
    private final Runnable exibirCabecalho;

    public AtributosController(
        FichaService service,
        MenuAtributos menu,
        Runnable limparTela,
        Runnable exibirCabecalho
    ) {
        this.service = service;
        this.menu = menu;
        this.limparTela = limparTela;
        this.exibirCabecalho = exibirCabecalho;
    }

    public void iniciar() {
        Ficha ficha = service.carregarFicha();
        boolean executando = true;

        while (executando) {
            limparTela.run();
            exibirCabecalho.run();
            menu.exibirAtributos(ficha.getAtributos());
            menu.exibirMenuEdicao();

            int opcao = menu.lerOpcao();
            if (opcao == 0) {
                executando = false;
                continue;
            }

            if (editar(ficha.getAtributos(), opcao)) {
                service.salvarFicha(ficha);
            }
            menu.exibirMensagem("Pressione Enter para continuar...");
            menu.aguardarEnter();
        }
        limparTela.run();
    }

    private boolean editar(Atributos atributos, int opcao) {
        if (opcao < 1 || opcao > 7) {
            menu.exibirMensagem("Opção inválida.");
            return false;
        }

        int valor = menu.lerInteiroPositivo();
        switch (opcao) {
            case 1 -> atributos.setInteligencia(valor);
            case 2 -> atributos.setSabedoria(valor);
            case 3 -> atributos.setForca(valor);
            case 4 -> atributos.setDestreza(valor);
            case 5 -> atributos.setConstituicao(valor);
            case 6 -> atributos.setCarisma(valor);
            case 7 -> atributos.setBonusProficiencia(valor);
            default -> throw new IllegalStateException("Opção de atributo inválida.");
        }

        menu.exibirMensagem("Atributo atualizado com sucesso.");
        return true;
    }
}
