package br.com.ficha.ui;

import java.util.Scanner;

import br.com.ficha.model.Anotacao;

public class MenuAnotacoes {
    private final Scanner scanner;

    public MenuAnotacoes(Scanner scanner) {
        this.scanner = scanner;
    }

    public void menu() {
        System.out.println("ANOTAÇÕES");
        System.out.println("1 - Exibir anotações");
        System.out.println("2 - Adicionar anotação");
        System.out.println("3 - Excluir anotação");
        System.out.println("4 - Editar anotação");
        System.out.println("0 - Voltar");
    }

    public int numero(String mensagem) {
        System.out.println(mensagem);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }

    public Anotacao ler(Anotacao atual) {
        String mensagemTitulo = atual == null
            ? "Título (Esc para cancelar):"
            : "Título atual: " + atual.getTitulo() + "\nNovo título (Esc para cancelar):";
        String titulo = texto(mensagemTitulo);
        if (titulo == null) return null;

        String mensagemTexto = atual == null
            ? "Texto (Esc para cancelar):"
            : "Texto atual: " + atual.getTexto() + "\nNovo texto (Esc para cancelar):";
        String conteudo = texto(mensagemTexto);
        return conteudo == null ? null : new Anotacao(titulo, conteudo);
    }

    public String texto(String mensagem) {
        System.out.println(mensagem);
        String valor = scanner.nextLine();
        if (valor.equals("\u001B")) return null;
        return valor.trim();
    }

    public void pausar() {
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
    }
}
