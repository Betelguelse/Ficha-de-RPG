package br.com.ficha;

import java.util.Scanner;

import br.com.ficha.controller.HabilidadesController;
import br.com.ficha.repository.HabilidadeRepository;
import br.com.ficha.service.HabilidadeService;
import br.com.ficha.ui.MenuHabilidades;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        iniciar(scanner);

        System.out.println("Saindo do sistema. Ate mais!");
        scanner.close();
    }

    public static void iniciar(Scanner scanner) {
        boolean continuar = true;
        clear();

        while (continuar) {
            clear();
            cabecalho();
            menuAtributos();
            menuInterativo();

            System.out.println("Selecione uma opcao:");
            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    System.out.println("Editar atributos");
                    menuEditarAtributos(scanner);
                    break;
                case 2:
                    System.out.println("Itens");
                    break;
                case 3:
                    System.out.println("Equipamentos");
                    break;
                case 4:
                    System.out.println("Habilidades");
                    clear();
                    abrirMenuHabilidades(scanner);
                    break;
                case 0:
                    continuar = false;
                    System.out.println("Saindo...");
                    clear();
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }
    }

    public static void cabecalho() {
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
        System.out.println("Bem-vindo ao sistema de ficha");
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    private static int lerOpcao(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, insira um numero.");
            }
        }
    }

    public static void menuAtributos() {
        System.out.println("Nome: ");
        System.out.println("Idade: ");
        System.out.println("Sexo: ");
        System.out.println("Vida: ");
        System.out.println("Status: ");
        System.out.println("Raca: ");
        System.out.println("Classe: ");
        System.out.println("Nivel: ");

        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    public static void menuInterativo() {
        System.out.println("1 - Editar atributos");
        System.out.println("2 - Itens");
        System.out.println("3 - Equipamentos");
        System.out.println("4 - Habilidades");
        System.out.println("0 - Sair");
    }

    public static void menuEditarAtributos(Scanner scanner) {
        boolean noMenu = true;

        while (noMenu) {
            clear();
            cabecalho();
            System.out.println("1 - Editar nome");
            System.out.println("2 - Editar idade");
            System.out.println("3 - Editar sexo");
            System.out.println("4 - Editar vida");
            System.out.println("5 - Editar status");
            System.out.println("6 - Editar raca");
            System.out.println("7 - Editar classe");
            System.out.println("8 - Editar nivel");
            System.out.println("0 - Voltar");

            int opcao = lerOpcao(scanner);
            if (opcao == 0) {
                noMenu = false;
            } else {
                System.out.println("Edicao de atributos ainda nao implementada.");
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Nao foi possivel limpar a tela!");
        }
    }

    private static void abrirMenuHabilidades(Scanner scanner) {
        HabilidadeRepository repository = new HabilidadeRepository();
        HabilidadeService service = new HabilidadeService(repository);
        MenuHabilidades view = new MenuHabilidades(scanner);
        HabilidadesController controller = new HabilidadesController(service, view, App::clear, App::cabecalho);
        controller.iniciar();
    }
}
