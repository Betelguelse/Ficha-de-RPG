package src.entidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

    private static void voltarMenuHabilidades() {
        System.out.println("Aperte 0 para voltar ao menu de habilidades");
        if(Integer.parseInt(scanner.nextLine()) == 0 || scanner.nextLine() == ""){
            App.clear();
            MenuHabilidades.menu(scanner);
        }
    }

    private static Scanner scanner = new Scanner( System.in);

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
        }catch (Exception e){
            System.out.println("Erro ao exibir habilidades: " + e.getMessage());
        }
    }

    public static void selecionarHabilidade(List<Habilidades> habilidades){
        exibirHabilidades(habilidades);
        System.out.println("\nEscolha uma habilidade para ver mais detalhes ou pressione 0 para voltar");
        int tecla = Integer.parseInt(scanner.nextLine());
        if(tecla > 0 && tecla <= habilidades.size()){
            Habilidades escolhida = habilidades.get(tecla - 1);
            System.out.println("\n" + escolhida.toString() + "\n");
            voltarMenuHabilidades();
        }else{
            System.out.println("Opção inválida");
        }
        if (tecla == 0 || scanner.nextLine() == ""){
            App.clear();
            MenuHabilidades.menu(scanner);  
        }
        voltarMenuHabilidades();
    }

    public static void adicionarHabilidade(List<Habilidades> habilidades){
        App.clear();
        App.cabecalho();
        System.out.println("Adicionar nova habilidade");
        StringBuilder novaHabilidade = new StringBuilder();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        for(Habilidades habilidade : habilidades) {
            if(habilidade.getNome().equalsIgnoreCase(nome)) {
                System.err.println("Nome de habilidade já existente. Por favor, escolha outro nome.");
                return;
            }
        }
        novaHabilidade.append(nome).append(";");

        System.out.print("Nível: ");
        int nivel = scanner.nextInt();
        novaHabilidade.append(nivel).append(";");
        scanner.nextLine(); 

        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();
        novaHabilidade.append(tipo).append(";");

        System.out.print("Recarga: ");
        String recarga = scanner.nextLine();
        novaHabilidade.append(recarga).append(";");

        System.out.print("Custo: ");
        String custo = scanner.nextLine();
        novaHabilidade.append(custo).append(";");

        System.out.print("Descrição: ");
        String desc = scanner.nextLine();
        novaHabilidade.append(desc);
        
        String caminho = "habilidades.csv";
        try(FileWriter writer = new FileWriter(caminho, true)){  
            writer.write(novaHabilidade.toString() + "\n"); 
        } catch (Exception e) {
            System.out.println("Erro ao adicionar habilidade: " + e.getMessage());
            return;
        }
        
        System.out.println("Habilidade adicionada com sucesso!");
        voltarMenuHabilidades();   
    }

    public static void editarHabilidade(List<Habilidades> habilidades) {
        
    }

    public static void excluirHabilidade(List<Habilidades> habilidades) {
        lerHabilidadesCSV(habilidades);

        if (habilidades.isEmpty()) {
            System.out.println("Nenhuma habilidade para excluir.");
            return;
        }

        // Mostra a lista numerada
        exibirHabilidades(habilidades);

        System.out.println("\nDigite o número da habilidade que deseja excluir (0 para cancelar):");
        String entrada = scanner.nextLine();
        int opcao;
        try {
            opcao = Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return;
        }

        if (opcao == 0) {
            System.out.println("Operação cancelada.");
            voltarMenuHabilidades();
        }

        if (opcao < 1 || opcao > habilidades.size()) {
            System.out.println("Número inválido.");
            voltarMenuHabilidades();
        }

        Habilidades removida = habilidades.remove(opcao - 1);

        // Lê o cabeçalho do CSV (se existir) para reescrever preservando-o
        String header = null;
        File arquivo = new File("habilidades.csv");
        if (arquivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                header = br.readLine(); // pode ser null se arquivo vazio
            } catch (IOException e) {
                // se falhar, continua sem cabeçalho
                header = null;
            }
        }

        // Reescreve o arquivo com a lista atualizada
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, false))) {
            if (header != null && !header.isBlank()) {
                bw.write(header);
                bw.newLine();
            }
            for (Habilidades h : habilidades) {
                // Ajuste os campos na mesma ordem do CSV
                bw.write(h.getNome() + ";" + h.getNivel() + ";" + h.getTipo() + ";" + h.getRecarga() + ";" + h.getCusto() + ";" + h.getDescricao());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
            return;
        }

        System.out.println("Habilidade '" + removida.getNome() + "' removida com sucesso.");
        voltarMenuHabilidades();
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