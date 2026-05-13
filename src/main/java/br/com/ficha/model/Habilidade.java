package br.com.ficha.model;

public class Habilidade {
    private String nome;
    private int nivel;
    private String tipo;
    private String recarga;
    private String custo;
    private String descricao;

    public Habilidade(String nome, int nivel, String tipo, String recarga, String custo, String descricao) {
        this.nome = nome;
        this.nivel = nivel;
        this.tipo = tipo;
        this.recarga = recarga;
        this.custo = custo;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public String getTipo() {
        return tipo;
    }

    public String getRecarga() {
        return recarga;
    }

    public String getCusto() {
        return custo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String paraLinhaCsv() {
        return String.join(";", nome, String.valueOf(nivel), tipo, recarga, custo, descricao);
    }

    @Override
    public String toString() {
        return "Nome: " + nome + "\n"
            + "Nivel: " + nivel + "\n"
            + "Tipo: " + tipo + "\n"
            + "Recarga: " + recarga + "\n"
            + "Custo: " + custo + "\n"
            + "Descricao: " + descricao;
    }
}
