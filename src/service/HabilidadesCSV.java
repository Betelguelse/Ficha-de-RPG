package src.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import src.entidades.Habilidades;

public class HabilidadesCSV {
    private static Scanner scanner = new Scanner(System.in);

    public static void lerHabilidadesCSV(List<Habilidades> habilidades){
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

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void exibirHabilidades(List<Habilidades> habilidades) {
        lerHabilidadesCSV(habilidades);
        if(habilidades.isEmpty()){
            System.out.println("Nenhuma habilidade encontrada");
        }
        for(Habilidades habilidade : habilidades){
            for(int i = 1; i <= habilidades.size() ; i++){
                System.out.println(  i + " - Nome: " + habilidade.getNome());
            }            
        }
        selecionarHabilidade(habilidades);
    }

    public static void selecionarHabilidade(List<Habilidades> habilidades){
        lerHabilidadesCSV(habilidades);
        System.out.println("Escolha uma habilidade para ver mais detalhes ou pressione 0 para voltar");
        int opcao = scanner.nextInt();
        if(opcao > 0 && opcao <= habilidades.size()){
            Habilidades escolhida = habilidades.get(opcao);
            escolhida.toString();
        }else{
            System.out.println("Opção inválida");
        }
    }
}
