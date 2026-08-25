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
    private final Atributos atributos;

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
        int moedaOuro,
        int inteligencia,
        int sabedoria,
        int forca,
        int destreza,
        int constituicao,
        int carisma,
        int bonusProficiencia
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
        this.atributos = new Atributos(inteligencia, sabedoria, forca, destreza, constituicao, carisma, bonusProficiencia);
    }

    public static Ficha padrao() {
        return new Ficha("", 0, "", 0, "", "", "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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

    public int getInteligencia() {
        return atributos.getInteligencia();
    }

    public Atributos getAtributos() {
        return atributos;
    }

    public void setInteligencia(int inteligencia) {
        atributos.setInteligencia(inteligencia);
    }

    public int getSabedoria() {
        return atributos.getSabedoria();
    }

    public void setSabedoria(int sabedoria) {
        atributos.setSabedoria(sabedoria);
    }

    public int getForca() {
        return atributos.getForca();
    }

    public void setForca(int forca) {
        atributos.setForca(forca);
    }

    public int getDestreza() {
        return atributos.getDestreza();
    }

    public void setDestreza(int destreza) {
        atributos.setDestreza(destreza);
    }

    public int getConstituicao() {
        return atributos.getConstituicao();
    }

    public void setConstituicao(int constituicao) {
        atributos.setConstituicao(constituicao);
    }

    public int getCarisma() {
        return atributos.getCarisma();
    }

    public void setCarisma(int carisma) {
        atributos.setCarisma(carisma);
    }

    public int getBonusProficiencia() {
        return atributos.getBonusProficiencia();
    }

    public void setBonusProficiencia(int bonusProficiencia) {
        atributos.setBonusProficiencia(bonusProficiencia);
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
            String.valueOf(moedaOuro),
            String.valueOf(atributos.getInteligencia()),
            String.valueOf(atributos.getSabedoria()),
            String.valueOf(atributos.getForca()),
            String.valueOf(atributos.getDestreza()),
            String.valueOf(atributos.getConstituicao()),
            String.valueOf(atributos.getCarisma()),
            String.valueOf(atributos.getBonusProficiencia())
        );
    }

    private static String sanitizar(String valor) {
        return valor == null ? "" : valor.replace(";", ",");
    }
}
