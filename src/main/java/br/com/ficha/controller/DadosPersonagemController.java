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

            if (editar(ficha, opcao)) {
                service.salvarFicha(ficha);
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
            case 4 -> ficha.setVida(menu.lerInteiroNaoNegativo("Digite a vida:"));
            case 5 -> ficha.setStatus(menu.lerTexto("Digite o status:"));
            case 6 -> ficha.setRaca(menu.lerTexto("Digite a raça:"));
            case 7 -> ficha.setClasse(menu.lerTexto("Digite a classe:"));
            case 8 -> ficha.setNivel(menu.lerInteiroNaoNegativo("Digite o nível:"));
            case 9 -> ficha.setMoedaBronze(menu.lerInteiroNaoNegativo("Digite a quantidade de moedas de bronze:"));
            case 10 -> ficha.setMoedaPrata(menu.lerInteiroNaoNegativo("Digite a quantidade de moedas de prata:"));
            case 11 -> ficha.setMoedaOuro(menu.lerInteiroNaoNegativo("Digite a quantidade de moedas de ouro:"));
            default -> {
                menu.exibirMensagem("Opção inválida.");
                return false;
            }
        }
        menu.exibirMensagem("Dados atualizados com sucesso.");
        return true;
    }
}
