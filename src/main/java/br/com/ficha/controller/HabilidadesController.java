package br.com.ficha.controller;

import java.util.List;

import br.com.ficha.model.Habilidade;
import br.com.ficha.service.HabilidadeService;
import br.com.ficha.ui.MenuHabilidades;

public class HabilidadesController {
    private final HabilidadeService habilidadeService;
    private final MenuHabilidades menuHabilidades;
    private final Runnable limparTela;
    private final Runnable exibirCabecalho;

    public HabilidadesController(
        HabilidadeService habilidadeService,
        MenuHabilidades menuHabilidades,
        Runnable limparTela,
        Runnable exibirCabecalho
    ) {
        this.habilidadeService = habilidadeService;
        this.menuHabilidades = menuHabilidades;
        this.limparTela = limparTela;
        this.exibirCabecalho = exibirCabecalho;
    }

    public void iniciar() {
        boolean executando = true;

        while (executando) {
            limparTela.run();
            exibirCabecalho.run();
            menuHabilidades.exibirMenu();

            int opcao = menuHabilidades.lerOpcao();
            switch (opcao) {
                case 1:
                    exibirHabilidades();
                    break;
                case 2:
                    adicionarHabilidade();
                    break;
                case 3:
                    excluirHabilidade();
                    break;
                case 0:
                    executando = false;
                    break;
                default:
                    menuHabilidades.exibirMensagem("Opcao invalida.");
                    aguardarContinuacao();
            }
        }
    }

    private void exibirHabilidades() {
        limparTela.run();
        exibirCabecalho.run();
        List<Habilidade> habilidades = habilidadeService.listarHabilidades();
        menuHabilidades.exibirLista(habilidades);

        if (!habilidades.isEmpty()) {
            int opcao = menuHabilidades.lerNumeroHabilidade("\nEscolha uma habilidade para ver detalhes ou 0 para voltar:");
            if (opcao > 0 && opcao <= habilidades.size()) {
                menuHabilidades.exibirDetalhes(habilidadeService.buscarPorIndice(opcao - 1));
            } else if (opcao != 0) {
                menuHabilidades.exibirMensagem("Opcao invalida.");
            }
        }

        aguardarContinuacao();
    }

    private void adicionarHabilidade() {
        limparTela.run();
        exibirCabecalho.run();

        try {
            Habilidade novaHabilidade = menuHabilidades.lerNovaHabilidade();
            habilidadeService.adicionarHabilidade(novaHabilidade);
            menuHabilidades.exibirMensagem("Habilidade adicionada com sucesso!");
        } catch (IllegalArgumentException e) {
            menuHabilidades.exibirMensagem(e.getMessage());
        }

        aguardarContinuacao();
    }

    private void excluirHabilidade() {
        limparTela.run();
        exibirCabecalho.run();

        List<Habilidade> habilidades = habilidadeService.listarHabilidades();
        menuHabilidades.exibirLista(habilidades);
        if (habilidades.isEmpty()) {
            aguardarContinuacao();
            return;
        }

        int opcao = menuHabilidades.lerNumeroHabilidade("\nDigite o numero da habilidade que deseja excluir ou 0 para cancelar:");
        if (opcao == 0) {
            return;
        }

        try {
            Habilidade removida = habilidadeService.excluirHabilidade(opcao - 1);
            menuHabilidades.exibirMensagem("Habilidade excluida com sucesso: " + removida.getNome());
        } catch (IllegalArgumentException e) {
            menuHabilidades.exibirMensagem(e.getMessage());
        }

        aguardarContinuacao();
    }

    private void aguardarContinuacao() {
        menuHabilidades.exibirMensagem("Pressione Enter para continuar...");
        menuHabilidades.aguardarEnter();
    }
}
