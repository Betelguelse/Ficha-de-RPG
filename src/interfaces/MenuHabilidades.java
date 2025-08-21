package src.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.App;
import src.entidades.Habilidades;

public class MenuHabilidades implements IMenu {
    private static Scanner scanner = new Scanner(System.in);
    static List<Habilidades> habilidades = new ArrayList<>();

    public static void menu(Scanner scanner){
        boolean condicao = true;

        App.clear();
        App.cabecalho();

        System.out.println("1 - Exibir habilidades");
        System.out.println("2 - Adicionar habilidades");
        System.out.println("3 - Editar habilidades");
        System.out.println("4 - Excluir habilidades");
        System.out.println("0 - Voltar");

        while(condicao){
            switch (scanner.nextInt()) {
            case 1:
                App.clear();
                Habilidades.exibirHabilidades(habilidades);
                break;
            case 2:
                App.clear();
                Habilidades.adicionarHabilidade(habilidades);
                break;
            case 3:
                break;
            case 4:
                break;
            case 0:
                condicao = false;
                App.iniciar(scanner);
            default:
                break;
            }
        }
        
    }
    
}
