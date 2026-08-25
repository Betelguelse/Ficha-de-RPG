package br.com.ficha.model;

public class Equipamento {
    private String nome;
    private final String tipo;
    private String categoria;
    private String dano;
    private String tipoDano;
    private String peso;
    private int valor;
    private String raridade;
    private String equipadoComo;

    public Equipamento(String nome, String tipo, String categoria, String dano, String tipoDano, String peso, int valor, String raridade, String equipadoComo) {
        this.nome = nome;
        this.tipo = tipo;
        this.categoria = categoria;
        this.dano = dano;
        this.tipoDano = tipoDano;
        this.peso = peso;
        this.valor = valor;
        this.raridade = raridade;
        this.equipadoComo = equipadoComo;
    }

    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public String getCategoria() { return categoria; }
    public String getDano() { return dano; }
    public String getTipoDano() { return tipoDano; }
    public String getPeso() { return peso; }
    public int getValor() { return valor; }
    public String getRaridade() { return raridade; }
    public String getEquipadoComo() { return equipadoComo; }
    public void setEquipadoComo(String equipadoComo) { this.equipadoComo = equipadoComo; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDano(String dano) { this.dano = dano; }
    public void setTipoDano(String tipoDano) { this.tipoDano = tipoDano; }
    public void setPeso(String peso) { this.peso = peso; }
    public void setValor(int valor) { this.valor = valor; }
    public void setRaridade(String raridade) { this.raridade = raridade; }

    public String paraLinhaCsv() {
        return String.join(";", limpar(nome), limpar(tipo), limpar(categoria), limpar(dano), limpar(tipoDano), limpar(peso), String.valueOf(valor), limpar(raridade), limpar(equipadoComo));
    }

    private String limpar(String texto) { return texto.replace(";", ",").replace("\n", " ").replace("\r", " "); }
}
