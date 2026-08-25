package br.com.ficha.controller;

import java.util.List;

import br.com.ficha.model.Anotacao;
import br.com.ficha.service.AnotacaoService;
import br.com.ficha.ui.MenuAnotacoes;

public class AnotacoesController {
    private final AnotacaoService service;
    private final MenuAnotacoes menu;
    private final Runnable limpar;
    private final Runnable cabecalho;

    public AnotacoesController(AnotacaoService service, MenuAnotacoes menu, Runnable limpar, Runnable cabecalho) {
        this.service = service;
        this.menu = menu;
        this.limpar = limpar;
        this.cabecalho = cabecalho;
    }

    public void iniciar() {
        boolean aberto = true;

        while (aberto) {
            limpar.run();
            cabecalho.run();
            menu.menu();

            int opcao = menu.numero("Selecione uma opção:");
            if (opcao == 0) {
                aberto = false;
                continue;
            }

            List<Anotacao> anotacoes = service.listar();
            if (opcao == 1) {
                listar(anotacoes);
            } else if (opcao == 2) {
                adicionar(anotacoes);
            } else if (opcao == 3) {
                excluir(anotacoes);
            } else if (opcao == 4) {
                editar(anotacoes);
            }
        }
    }

    private void listar(List<Anotacao> anotacoes) {
        if (anotacoes.isEmpty()) {
            System.out.println("Nenhuma anotação encontrada.");
        } else {
            exibirLista(anotacoes);
            int numero = menu.numero("Número ou 0:");
            if (numero > 0 && numero <= anotacoes.size()) {
                Anotacao anotacao = anotacoes.get(numero - 1);
                System.out.println("\nTítulo: " + anotacao.getTitulo());
                System.out.println("Texto: " + anotacao.getTexto());
            }
        }
        menu.pausar();
    }

    private void adicionar(List<Anotacao> anotacoes) {
        Anotacao anotacao = menu.ler(null);
        if (anotacao != null) {
            anotacoes.add(anotacao);
            service.salvar(anotacoes);
        }
    }

    private void excluir(List<Anotacao> anotacoes) {
        if (anotacoes.isEmpty()) {
            System.out.println("Nenhuma anotação encontrada.");
            menu.pausar();
            return;
        }

        exibirLista(anotacoes);
        int numero = menu.numero("Número ou 0:");
        if (numero > 0 && numero <= anotacoes.size()) {
            anotacoes.remove(numero - 1);
            service.salvar(anotacoes);
        }
    }

    private void editar(List<Anotacao> anotacoes) {
        if (anotacoes.isEmpty()) {
            System.out.println("Nenhuma anotação encontrada.");
            menu.pausar();
            return;
        }

        exibirLista(anotacoes);
        int numero = menu.numero("Número ou 0:");
        if (numero > 0 && numero <= anotacoes.size()) {
            Anotacao atualizada = menu.ler(anotacoes.get(numero - 1));
            if (atualizada != null) {
                anotacoes.set(numero - 1, atualizada);
                service.salvar(anotacoes);
            }
        }
    }

    private void exibirLista(List<Anotacao> anotacoes) {
        for (int i = 0; i < anotacoes.size(); i++) {
            System.out.println((i + 1) + " - " + anotacoes.get(i).getTitulo());
        }
    }
}
