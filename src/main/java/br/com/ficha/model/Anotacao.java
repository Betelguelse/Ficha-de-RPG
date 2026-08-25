package br.com.ficha.model;
public class Anotacao { private String titulo, texto; public Anotacao(String titulo,String texto){this.titulo=titulo;this.texto=texto;} public String getTitulo(){return titulo;} public String getTexto(){return texto;} public String csv(){return titulo.replace(";",",")+";"+texto.replace(";",",").replace("\n"," ");} }
