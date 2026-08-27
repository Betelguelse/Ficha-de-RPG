package br.com.ficha.ui;

import java.util.List;
import java.util.Scanner;

import br.com.ficha.model.Item;

public class MenuInventario {
    private final Scanner scanner;

    public MenuInventario(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibir(List<Item> itens) {
        System.out.println("INVENTÁRIO");
        System.out.println();

        if (itens.isEmpty()) {
            System.out.println("O inventário está vazio.");
            return;
        }

        for (Item item : itens) {
            System.out.println(item.getNome() + " x" + item.getQuantidade());
        }
    }

    public void aguardarEnter() {
        System.out.println();
        System.out.println("Pressione Enter para voltar...");
        scanner.nextLine();
    }
}
