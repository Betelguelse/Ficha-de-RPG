package src;
import java.util.Scanner;

import src.interfaces.MenuHabilidades;

public class App {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        iniciar(scanner);
        
        System.out.println("Saindo do sistema. Até mais!");
        scanner.close();
    }

    public static void iniciar(Scanner scanner) {
        int opcao;
        boolean continuar = true;
        clear();
        
        while(continuar){

            clear();
            cabecalho();
            menuAtributos();
            menuInterativo();

            System.out.println("Selecione uma opção:");
            entradaInvalida(scanner);

            switch(opcao = Integer.parseInt(scanner.nextLine())) {
                case 1:
                    System.out.println("Editar atributos");
                    menuEditarAtributos(scanner);
                    // Lógica para editar atributos
                    break;
                case 2:
                    System.out.println("Itens");
                    // Lógica para itens
                    break;
                case 3:
                    System.out.println("Equipamentos");
                    // Lógica para equipamentos
                    break;
                case 4:
                    System.out.println("Habilidades");
                    // Lógica para habilidades
                    clear();
                    MenuHabilidades.menu(scanner);
                    break;
                case 0:
                    continuar = false;
                    System.out.println("Saindo...");
                    clear();
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public static void cabecalho(){
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
        System.out.println("Bem-vindo ao sistema de ficha");
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    public static void entradaInvalida(Scanner scanner){
        if(!scanner.hasNextInt()){
            System.out.println("\nEntrada inválida. Por favor, insira um número.");
            scanner.next();       
        }
    }

    public static void voltar(Scanner scanner){
        clear();
        if(scanner.nextInt() == 0){
            System.out.println("Voltando ao menu principal...");
            menuInterativo();
        }
    }

    public static void menuAtributos(){
        System.out.println("Nome: ");
        System.out.println("Idade: ");
        System.out.println("Sexo: ");
        System.out.println("Vida: ");
        System.out.println("Status: ");
        System.out.println("Raça: ");
        System.out.println("Classe: ");
        System.out.println("Nível: ");

        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    public static void menuInterativo(){
        
        System.out.println("1 - Editar atributos");
        System.out.println("2 - Itens");
        System.out.println("3 - Equipamentos");
        System.out.println("4 - Habilidades");
        System.out.println("0 - Sair");

    }

    public static void menuEditarAtributos(Scanner scanner){
        clear();
        cabecalho();
        System.out.println("1 - Editar nome");
        System.out.println("2 - Editar idade");
        System.out.println("3 - Editar sexo");
        System.out.println("4 - Editar vida");
        System.out.println("5 - Editar status");
        System.out.println("6 - Editar raça");
        System.out.println("7 - Editar classe");
        System.out.println("8 - Editar nível");
        System.out.println("0 - Voltar");
        
        voltar(new Scanner(System.in));
    }   

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Não foi possível limpar a tela!");
        }
    }   
}