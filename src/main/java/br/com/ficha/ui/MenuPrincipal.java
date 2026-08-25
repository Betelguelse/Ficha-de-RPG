package br.com.ficha.ui;

import java.util.Scanner;

public class MenuPrincipal {
    private final Scanner scanner;

    public MenuPrincipal(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibir() {
        System.out.println("1 - Editar dados personagem");
        System.out.println("2 - Atributos");
        System.out.println("3 - Itens");
        System.out.println("4 - Equipamentos");
        System.out.println("5 - Habilidades");
        System.out.println("6 - Anotações");
        System.out.println("0 - Sair");
    }

    public int lerOpcao() {
        System.out.println("Selecione uma opção:");
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
