package src.entidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

import src.App;
import src.interfaces.MenuHabilidades;

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

    private static Scanner scanner = new Scanner(System.in);

    public static void lerHabilidadesCSV(List<Habilidades> habilidades) {
        habilidades.clear();
        try(BufferedReader br = new BufferedReader(new FileReader("habilidades.csv"))){
            String linha;
            br.readLine(); // Pular o cabeçalho
            while((linha = br.readLine()) != null){
                String[] valores = linha.split(";");
                if(valores.length == 6){
                    String nome = valores[0];
                    int nivel = Integer.parseInt(valores[1]);
                    String tipo = valores[2];
                    String recarga = valores[3];
                    String custo = valores[4];
                    String descricao = valores[5];

                    Habilidades habilidade = new Habilidades(nome, nivel, tipo, recarga, custo, descricao);
                    habilidades.add(habilidade);
                }
            }
        } catch (Exception e){
            System.out.println("Erro ao criar lista: " + e.getMessage());
        }
       
    }

    public static void exibirHabilidades(List<Habilidades> habilidades) {
        lerHabilidadesCSV(habilidades);  
        try{
            App.clear();
            App.cabecalho();
            if(habilidades.isEmpty()){
                System.out.println("Nenhuma habilidade encontrada");
                return;
            }
            for(int i = 0; i < habilidades.size() ; i++){
                System.out.println((i + 1) + " - Nome: " + habilidades.get(i).getNome());
            }            
            selecionarHabilidade(habilidades);
        }catch (Exception e){
            System.out.println("Erro ao exibir habilidades: " + e.getMessage());
        }
    }

    public static void selecionarHabilidade(List<Habilidades> habilidades){
        System.out.println("\nEscolha uma habilidade para ver mais detalhes ou pressione 0 para voltar");
        int tecla = scanner.nextInt();
        if(tecla > 0 && tecla <= habilidades.size()){
            Habilidades escolhida = habilidades.get(tecla - 1);
            System.out.println("\n" + escolhida.toString());
        }else if(tecla == 0){
            App.clear();
            MenuHabilidades.menu(scanner);
        }else{
            System.out.println("Opção inválida");
        }
    }

    public static void adicionarHabilidade(List<Habilidades> habilidades){
        App.clear();
        App.cabecalho();
        System.out.println("Adicionar nova habilidade");
        StringBuilder novaHabilidade = new StringBuilder();

        System.out.print("Nome: ");
        novaHabilidade.append(scanner.next());
        novaHabilidade.append(";");
        
        System.out.print("Nível: ");
        novaHabilidade.append(scanner.next());
        novaHabilidade.append(";");

        System.out.print("Tipo: ");
        novaHabilidade.append(scanner.next());
        novaHabilidade.append(";");

        System.out.print("Recarga: ");
        novaHabilidade.append(scanner.next());
        novaHabilidade.append(";");

        System.out.print("Custo: ");
        novaHabilidade.append(scanner.next());
        novaHabilidade.append(";");

        System.out.print("Descrição: ");
        novaHabilidade.append(scanner.next());
        
        String caminho = "habilidades.csv";
        try(FileWriter writer = new FileWriter(caminho, true)){  
            writer.write(novaHabilidade.toString() + "\n"); 
        } catch (Exception e) {
            System.out.println("Erro ao adicionar habilidade: " + e.getMessage());
            return;
        }
        
        System.out.println("Habilidade adicionada com sucesso!");
        MenuHabilidades.menu(scanner);
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