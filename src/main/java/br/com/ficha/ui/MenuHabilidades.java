package br.com.ficha.ui;

import java.util.List;
import java.util.Scanner;

import br.com.ficha.model.Habilidade;

public class MenuHabilidades {
    private final Scanner scanner;

    public MenuHabilidades(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibirMenu() {
        System.out.println("1 - Exibir habilidades");
        System.out.println("2 - Adicionar habilidades");
        System.out.println("3 - Excluir habilidades");
        System.out.println("4 - Editar habilidade");
        System.out.println("0 - Voltar");
    }

    public void exibirLista(List<Habilidade> habilidades) {
        if (habilidades.isEmpty()) {
            System.out.println("Nenhuma habilidade encontrada.");
            return;
        }

        for (int i = 0; i < habilidades.size(); i++) {
            System.out.println((i + 1) + " - Nome: " + habilidades.get(i).getNome());
        }
    }

    public void exibirDetalhes(Habilidade habilidade) {
        System.out.println();
        System.out.println(habilidade);
        System.out.println();
    }

    public Habilidade lerNovaHabilidade() {
        System.out.println("Adicionar nova habilidade");

        System.out.println("Nome (Esc para cancelar): ");
        String nome = lerStringVazia();
        if (nome == null) return null;

        System.out.print("Nível (Esc para cancelar): ");
        int nivel = lerInteiro();
        if (nivel == Integer.MIN_VALUE) return null;

        System.out.print("Tipo (Esc para cancelar): ");
        String tipo = lerStringVazia();
        if (tipo == null) return null;

        System.out.print("Alcance (Esc para cancelar): ");
        String alcance = lerStringVazia();
        if (alcance == null) return null;

        System.out.print("Recarga (Esc para cancelar): ");
        String recarga = lerStringVazia();
        if (recarga == null) return null;

        System.out.print("Custo (Esc para cancelar): ");
        String custo = lerStringVazia();
        if (custo == null) return null;

        System.out.print("Descrição (Esc para cancelar): ");
        String descricao = lerStringVazia();
        if (descricao == null) return null;

        return new Habilidade(nome, nivel, tipo, alcance, recarga, custo, descricao);
    }

    public int lerOpcao() {
        return lerInteiro();
    }

    public int lerNumeroHabilidade(String mensagem) {
        System.out.println(mensagem);
        return lerInteiro();
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void aguardarEnter() {
        scanner.nextLine();
    }

    private String lerStringVazia() {
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
