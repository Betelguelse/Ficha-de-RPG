package br.com.ficha.model;

public class Anotacao {
    private final String titulo;
    private final String texto;

    public Anotacao(String titulo, String texto) {
        this.titulo = titulo;
        this.texto = texto;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTexto() {
        return texto;
    }

    public String csv() {
        return sanitizar(titulo) + ";" + sanitizar(texto);
    }

    private String sanitizar(String valor) {
        return valor.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }
}
