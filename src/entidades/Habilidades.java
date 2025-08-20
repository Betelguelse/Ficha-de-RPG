package src.entidades;

public class Habilidades {
    
    private String nome;
    private int nivel;
    private String tipo;
    private String recarga;
    private String custo;
    private String descricao;

    public Habilidades(){

    }

    public Habilidades(String nome, int nivel, String tipo, String recarga, String custo, String descricao) {
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNivel() {
        return nivel;
    }
    
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;   
    }
    
    public String getRecarga() {
        return recarga;
    }
    
    public void setRecarga(String recarga) {
        this.recarga = recarga;
    }
    
    public String getCusto() {
        return custo;
    }
    
    public void setCusto(String custo) {
        this.custo = custo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return  ("Nome: " + nome +
                "\nNível: " + nivel +
                "\nTipo: " + tipo + 
                "\nRecarga: " + recarga +
                "\nCusto: " + custo +
                "\nDescrição: " + descricao);
    }
} 