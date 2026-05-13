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
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Nivel: ");
        int nivel = lerInteiro();

        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();

        System.out.print("Recarga: ");
        String recarga = scanner.nextLine();

        System.out.print("Custo: ");
        String custo = scanner.nextLine();

        System.out.print("Descricao: ");
        String descricao = scanner.nextLine();

        return new Habilidade(nome, nivel, tipo, recarga, custo, descricao);
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

    private int lerInteiro() {
        while (true) {
            String entrada = scanner.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite um numero.");
            }
        }
    }
}
