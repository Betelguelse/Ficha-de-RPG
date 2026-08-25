package br.com.ficha.ui;

import java.util.Scanner;

import br.com.ficha.model.Atributos;

public class MenuAtributos {
    private final Scanner scanner;

    public MenuAtributos(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibirAtributos(Atributos atributos) {
        System.out.println("ATRIBUTOS");
        System.out.println();
        System.out.println("+----------------------------------+");
        System.out.printf("| BÔNUS DE PROFICIÊNCIA: %-8d |%n", atributos.getBonusProficiencia());
        System.out.println("+----------------------------------+");
        System.out.println();
        System.out.println("Inteligência: " + atributos.getInteligencia());
        System.out.println("Sabedoria: " + atributos.getSabedoria());
        System.out.println("Força: " + atributos.getForca());
        System.out.println("Destreza: " + atributos.getDestreza());
        System.out.println("Constituição: " + atributos.getConstituicao());
        System.out.println("Carisma: " + atributos.getCarisma());
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    public void exibirMenuEdicao() {
        System.out.println("Editar Atributos");
        System.out.println();
        System.out.println("1 - Editar Inteligência");
        System.out.println("2 - Editar Sabedoria");
        System.out.println("3 - Editar Força");
        System.out.println("4 - Editar Destreza");
        System.out.println("5 - Editar Constituição");
        System.out.println("6 - Editar Carisma");
        System.out.println("7 - Editar Bônus de Proficiência");
        System.out.println("0 - Voltar");
    }

    public int lerOpcao() {
        return lerInteiro();
    }

    public int lerInteiroPositivo() {
        System.out.println("Digite um número inteiro positivo:");
        while (true) {
            int valor = lerInteiro();
            if (valor > 0) return valor;
            System.out.println("Entrada inválida. Digite um número inteiro positivo.");
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void aguardarEnter() {
        scanner.nextLine();
    }

    private int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }
}
