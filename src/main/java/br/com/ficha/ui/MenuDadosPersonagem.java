package br.com.ficha.ui;

import java.util.Scanner;

import br.com.ficha.model.Ficha;

public class MenuDadosPersonagem {
    private final Scanner scanner;

    public MenuDadosPersonagem(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibirDados(Ficha ficha) {
        System.out.println("DADOS PERSONAGEM");
        System.out.println();
        System.out.println("Nome: " + ficha.getNome());
        System.out.println("Idade: " + ficha.getIdade());
        System.out.println("Sexo: " + ficha.getSexo());
        System.out.println("Vida: " + ficha.getVida());
        System.out.println("Status: " + ficha.getStatus());
        System.out.println("Raça: " + ficha.getRaca());
        System.out.println("Classe: " + ficha.getClasse());
        System.out.println("Nível: " + ficha.getNivel());
        exibirMoedas(ficha);
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    public void exibirMenuEdicao() {
        System.out.println("Editar Dados Personagem");
        System.out.println();
        System.out.println("1 - Editar nome");
        System.out.println("2 - Editar idade");
        System.out.println("3 - Editar sexo");
        System.out.println("4 - Editar vida");
        System.out.println("5 - Editar status");
        System.out.println("6 - Editar raça");
        System.out.println("7 - Editar classe");
        System.out.println("8 - Editar nível");
        System.out.println("9 - Editar moeda de bronze");
        System.out.println("10 - Editar moeda de prata");
        System.out.println("11 - Editar moeda de ouro");
        System.out.println("0 - Voltar");
    }

    public int lerOpcao() {
        return lerInteiro("Entrada inválida. Digite um número.");
    }

    public String lerTexto(String mensagem) {
        System.out.println(mensagem);
        return scanner.nextLine().trim();
    }

    public int lerInteiroNaoNegativo(String mensagem) {
        System.out.println(mensagem);
        while (true) {
            int valor = lerInteiro("Entrada inválida. Digite um número inteiro maior ou igual a zero.");
            if (valor >= 0) return valor;
            System.out.println("Entrada inválida. Digite um número inteiro maior ou igual a zero.");
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void aguardarEnter() {
        scanner.nextLine();
    }

    private void exibirMoedas(Ficha ficha) {
        System.out.println();
        System.out.println("+---------------------+");
        System.out.println("|       MOEDAS        |");
        System.out.println("+------------+--------+");
        System.out.printf("| %-10s | %-6d |%n", "Bronze", ficha.getMoedaBronze());
        System.out.printf("| %-10s | %-6d |%n", "Prata", ficha.getMoedaPrata());
        System.out.printf("| %-10s | %-6d |%n", "Ouro", ficha.getMoedaOuro());
        System.out.println("+------------+--------+");
    }

    private int lerInteiro(String mensagemErro) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(mensagemErro);
            }
        }
    }
}
