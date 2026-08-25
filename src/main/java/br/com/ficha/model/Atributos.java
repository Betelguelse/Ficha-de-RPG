package br.com.ficha.model;

public class Atributos {
    private int inteligencia;
    private int sabedoria;
    private int forca;
    private int destreza;
    private int constituicao;
    private int carisma;
    private int bonusProficiencia;

    public Atributos(int inteligencia, int sabedoria, int forca, int destreza, int constituicao, int carisma, int bonusProficiencia) {
        this.inteligencia = inteligencia;
        this.sabedoria = sabedoria;
        this.forca = forca;
        this.destreza = destreza;
        this.constituicao = constituicao;
        this.carisma = carisma;
        this.bonusProficiencia = bonusProficiencia;
    }

    public int getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(int inteligencia) {
        this.inteligencia = inteligencia;
    }

    public int getSabedoria() {
        return sabedoria;
    }

    public void setSabedoria(int sabedoria) {
        this.sabedoria = sabedoria;
    }

    public int getForca() {
        return forca;
    }

    public void setForca(int forca) {
        this.forca = forca;
    }

    public int getDestreza() {
        return destreza;
    }

    public void setDestreza(int destreza) {
        this.destreza = destreza;
    }

    public int getConstituicao() {
        return constituicao;
    }

    public void setConstituicao(int constituicao) {
        this.constituicao = constituicao;
    }

    public int getCarisma() {
        return carisma;
    }

    public void setCarisma(int carisma) {
        this.carisma = carisma;
    }

    public int getBonusProficiencia() {
        return bonusProficiencia;
    }

    public void setBonusProficiencia(int bonusProficiencia) {
        this.bonusProficiencia = bonusProficiencia;
    }
}
