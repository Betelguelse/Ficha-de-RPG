package br.com.ficha.model;

public class Item {
    private final String nome;
    private final int quantidade;
    private final String tipo;
    private final String descricao;

    public Item(String nome, int quantidade, String tipo, String descricao) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String paraLinhaCsv() {
        return String.join(";", sanitizar(nome), String.valueOf(quantidade), sanitizar(tipo), sanitizar(descricao));
    }

    private static String sanitizar(String valor) {
        return valor.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }

    @Override
    public String toString() {
        return "Nome: " + nome + "\n"
            + "Quantidade: " + quantidade + "\n"
            + "Tipo: " + tipo + "\n"
            + "Descrição: " + descricao;
    }
}
