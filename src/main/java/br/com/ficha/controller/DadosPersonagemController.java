package br.com.ficha.controller;

import br.com.ficha.model.Ficha;
import br.com.ficha.service.FichaService;
import br.com.ficha.ui.MenuDadosPersonagem;

public class DadosPersonagemController {
    private final FichaService service;
    private final MenuDadosPersonagem menu;
    private final Runnable limparTela;
    private final Runnable exibirCabecalho;

    public DadosPersonagemController(
        FichaService service,
        MenuDadosPersonagem menu,
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
            menu.exibirDados(ficha);
            menu.exibirMenuEdicao();

            int opcao = menu.lerOpcao();
            if (opcao == 0) {
                executando = false;
                continue;
            }

            try {
                if (editar(ficha, opcao)) {
                    service.salvarFicha(ficha);
                }
            } catch (IllegalArgumentException e) {
                menu.exibirMensagem(e.getMessage());
            }
            menu.exibirMensagem("Pressione Enter para continuar...");
            menu.aguardarEnter();
        }
        limparTela.run();
    }

    private boolean editar(Ficha ficha, int opcao) {
        switch (opcao) {
            case 1 -> ficha.setNome(menu.lerTexto("Digite o nome:"));
            case 2 -> ficha.setIdade(menu.lerInteiroNaoNegativo("Digite a idade:"));
            case 3 -> ficha.setSexo(menu.lerTexto("Digite o sexo:"));
            case 4 -> service.atualizarVidaMaxima(ficha, menu.lerInteiroNaoNegativo("Digite a vida máxima:"));
            case 5 -> service.atualizarVidaTemporaria(ficha, menu.lerInteiroNaoNegativo("Digite a vida temporária:"));
            case 6 -> service.atualizarVidaAtual(ficha, menu.lerInteiroNaoNegativo("Digite a vida atual:"));
            case 7 -> ficha.setStatus(menu.lerTexto("Digite o status:"));
            case 8 -> ficha.setRaca(menu.lerTexto("Digite a raça:"));
            case 9 -> ficha.setClasse(menu.lerTexto("Digite a classe:"));
            case 10 -> ficha.setNivel(menu.lerInteiroNaoNegativo("Digite o nível:"));
            case 11 -> ficha.setMoedaBronze(menu.lerInteiroNaoNegativo("Digite a quantidade de moedas de bronze:"));
            case 12 -> ficha.setMoedaPrata(menu.lerInteiroNaoNegativo("Digite a quantidade de moedas de prata:"));
            case 13 -> ficha.setMoedaOuro(menu.lerInteiroNaoNegativo("Digite a quantidade de moedas de ouro:"));
            default -> {
                menu.exibirMensagem("Opção inválida.");
                return false;
            }
        }
        menu.exibirMensagem("Dados atualizados com sucesso.");
        return true;
    }
}
