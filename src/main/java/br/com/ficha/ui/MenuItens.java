package br.com.ficha.ui;

import java.util.List;
import java.util.Scanner;

import br.com.ficha.model.Item;

public class MenuItens {
    private static final String[] TIPOS = {
        "Arma", "Defesa", "Utilizável", "Ferramenta", "Material", "Acessório", "Outro"
    };

    private final Scanner scanner;

    public MenuItens(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibirMenu() {
        System.out.println("1 - Exibir itens");
        System.out.println("2 - Adicionar item");
        System.out.println("3 - Excluir item");
        System.out.println("4 - Editar item");
        System.out.println("0 - Voltar");
    }

    public void exibirLista(List<Item> itens) {
        if (itens.isEmpty()) {
            System.out.println("Nenhum item encontrado.");
            return;
        }

        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            System.out.println((i + 1) + " - " + item.getNome() + " (" + item.getQuantidade() + ")");
        }
    }

    public Item lerNovoItem() {
        System.out.println("Adicionar novo item");
        System.out.println();
        System.out.print("1. Nome do item (Esc para cancelar): ");
        String nome = lerTextoObrigatorio();
        if (nome == null) return null;

        System.out.print("2. Quantidade (Esc para cancelar): ");
        int quantidade = lerInteiroPositivo();
        if (quantidade == 0) return null;

        System.out.println("3. Tipo:");
        for (int i = 0; i < TIPOS.length; i++) {
            System.out.println((i + 1) + " - " + TIPOS[i]);
        }
        System.out.print("Escolha o tipo (Esc para cancelar): ");
        int opcaoTipo = lerOpcaoTipo();
        if (opcaoTipo == 0) return null;
        String tipo = TIPOS[opcaoTipo - 1];

        System.out.print("4. Descrição (Esc para cancelar): ");
        String descricao = lerTextoObrigatorio();
        if (descricao == null) return null;

        return new Item(nome, quantidade, tipo, descricao);
    }

    public void exibirDetalhes(Item item) {
        System.out.println();
        System.out.println(item);
        System.out.println();
    }

    public int lerOpcao() {
        return lerInteiro();
    }

    public int lerNumeroItem(String mensagem) {
        System.out.println(mensagem);
        return lerInteiro();
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void aguardarEnter() {
        scanner.nextLine();
    }

    private String lerTextoObrigatorio() {
        String entrada = scanner.nextLine();
        if (entrada.equals("\u001B")) return null;
        entrada = entrada.trim();
        while (entrada.isEmpty()) {
            System.out.println("Entrada inválida. Digite um texto válido.");
            entrada = scanner.nextLine();
            if (entrada.equals("\u001B")) return null;
            entrada = entrada.trim();
        }
        return entrada;
    }

    private int lerInteiroPositivo() {
        while (true) {
            int valor = lerInteiro();
            if (valor == Integer.MIN_VALUE) return 0;
            if (valor > 0) {
                return valor;
            }
            System.out.println("Entrada inválida. Digite um número inteiro positivo.");
        }
    }

    private int lerOpcaoTipo() {
        while (true) {
            int opcao = lerInteiro();
            if (opcao == Integer.MIN_VALUE) return 0;
            if (opcao >= 1 && opcao <= TIPOS.length) {
                return opcao;
            }
            System.out.println("Opção inválida. Escolha um tipo da lista.");
        }
    }

    private int lerInteiro() {
        while (true) {
            String entrada = scanner.nextLine();
            if (entrada.equals("\u001B")) return Integer.MIN_VALUE;
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }
}
