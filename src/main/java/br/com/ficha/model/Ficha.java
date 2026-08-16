package br.com.ficha.model;

public class Ficha {
    private String nome;
    private int idade;
    private String sexo;
    private int vida;
    private String status;
    private String raca;
    private String classe;
    private int nivel;
    private int moedaBronze;
    private int moedaPrata;
    private int moedaOuro;

    public Ficha(
        String nome,
        int idade,
        String sexo,
        int vida,
        String status,
        String raca,
        String classe,
        int nivel,
        int moedaBronze,
        int moedaPrata,
        int moedaOuro
    ) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.vida = vida;
        this.status = status;
        this.raca = raca;
        this.classe = classe;
        this.nivel = nivel;
        this.moedaBronze = moedaBronze;
        this.moedaPrata = moedaPrata;
        this.moedaOuro = moedaOuro;
    }

    public static Ficha padrao() {
        return new Ficha("", 0, "", 0, "", "", "", 0, 0, 0, 0);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getMoedaBronze() {
        return moedaBronze;
    }

    public void setMoedaBronze(int moedaBronze) {
        this.moedaBronze = moedaBronze;
    }

    public int getMoedaPrata() {
        return moedaPrata;
    }

    public void setMoedaPrata(int moedaPrata) {
        this.moedaPrata = moedaPrata;
    }

    public int getMoedaOuro() {
        return moedaOuro;
    }

    public void setMoedaOuro(int moedaOuro) {
        this.moedaOuro = moedaOuro;
    }

    public String paraLinhaCsv() {
        return String.join(";",
            sanitizar(nome),
            String.valueOf(idade),
            sanitizar(sexo),
            String.valueOf(vida),
            sanitizar(status),
            sanitizar(raca),
            sanitizar(classe),
            String.valueOf(nivel),
            String.valueOf(moedaBronze),
            String.valueOf(moedaPrata),
            String.valueOf(moedaOuro)
        );
    }

    private static String sanitizar(String valor) {
        return valor == null ? "" : valor.replace(";", ",");
    }
}
