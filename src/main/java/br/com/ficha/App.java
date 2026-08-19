package br.com.ficha;

import java.util.Scanner;

import br.com.ficha.controller.HabilidadesController;
import br.com.ficha.model.Ficha;
import br.com.ficha.repository.FichaRepository;
import br.com.ficha.repository.HabilidadeRepository;
import br.com.ficha.service.FichaService;
import br.com.ficha.service.HabilidadeService;
import br.com.ficha.ui.MenuHabilidades;

public class App {
    private static final int TERMINAL_ROWS = 20;
    private static final int TERMINAL_COLUMNS = 72;
    private static final Scanner scanner = new Scanner(System.in);
    private static final FichaService fichaService = new FichaService(new FichaRepository());

    public static void main(String[] args) {
        ajustarTamanhoTerminal();
        iniciar(scanner);

        System.out.println("Saindo do sistema. Ate mais!");
        scanner.close();
    }

    public static void iniciar(Scanner scanner) {
        boolean continuar = true;
        clear();

        while (continuar) {
            Ficha ficha = fichaService.carregarFicha();
            clear();
            cabecalho();
            menuAtributos(ficha);
            menuInterativo();

            System.out.println("Selecione uma opcao:");
            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    clear();
                    menuEditarAtributos(scanner);
                    break;
                case 2:
                    abrirMenuItens(scanner);
                    break;
                case 3:
                    abrirMenuEquipamentos(scanner);
                    break;
                case 4:
                    clear();
                    abrirMenuHabilidades(scanner);
                    break;
                case 5:
                    clear();
                    abrirMenuAnotacoes(scanner);
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
        System.out.println("            Bem-vindo ao sistema de ficha");
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

    public static void menuAtributos(Ficha ficha) {
        System.out.println("Nome: " + ficha.getNome());
        System.out.println("Idade: " + ficha.getIdade());
        System.out.println("Sexo: " + ficha.getSexo());
        System.out.println("Vida: " + ficha.getVida());
        System.out.println("Status: " + ficha.getStatus());
        System.out.println("Raça: " + ficha.getRaca());
        System.out.println("Classe: " + ficha.getClasse());
        System.out.println("Nível: " + ficha.getNivel());
        exibirTabelaMoedas(ficha);

        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
    }

    private static void exibirTabelaMoedas(Ficha ficha) {
        System.out.println();
        System.out.println("+---------------------+");
        System.out.println("|       MOEDAS        |");
        System.out.println("+------------+--------+");
        System.out.printf("| %-10s | %-6d |%n", "Bronze", ficha.getMoedaBronze());
        System.out.printf("| %-10s | %-6d |%n", "Prata", ficha.getMoedaPrata());
        System.out.printf("| %-10s | %-6d |%n", "Ouro", ficha.getMoedaOuro());
        System.out.println("+------------+--------+");
    }

    public static void menuInterativo() {
        System.out.println("1 - Editar atributos");
        System.out.println("2 - Itens");
        System.out.println("3 - Equipamentos");
        System.out.println("4 - Habilidades");
        System.out.println("5 - Anotações");
        System.out.println("0 - Sair");
    }

    public static void menuEditarAtributos(Scanner scanner) {
        boolean noMenu = true;
        Ficha ficha = fichaService.carregarFicha();

        while (noMenu) {
            clear();
            cabecalho();
            menuAtributos(ficha);
            System.out.println("1 - Editar nome");
            System.out.println("2 - Editar idade");
            System.out.println("3 - Editar sexo");
            System.out.println("4 - Editar vida");
            System.out.println("5 - Editar status");
            System.out.println("6 - Editar raca");
            System.out.println("7 - Editar classe");
            System.out.println("8 - Editar nivel");
            System.out.println("9 - Editar moeda de bronze");
            System.out.println("10 - Editar moeda de prata");
            System.out.println("11 - Editar moeda de ouro");
            System.out.println("0 - Voltar");

            int opcao = lerOpcao(scanner);
            if (opcao == 0) {
                noMenu = false;
                clear();
            } else {
                editarAtributo(scanner, ficha, opcao);
                fichaService.salvarFicha(ficha);
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void editarAtributo(Scanner scanner, Ficha ficha, int opcao) {
        switch (opcao) {
            case 1:
                System.out.println("Digite o nome:");
                ficha.setNome(scanner.nextLine().trim());
                System.out.println("Nome atualizado com sucesso.");
                break;
            case 2:
                System.out.println("Digite a idade:");
                ficha.setIdade(lerInteiroPositivo(scanner));
                System.out.println("Idade atualizada com sucesso.");
                break;
            case 3:
                System.out.println("Digite o sexo:");
                ficha.setSexo(scanner.nextLine().trim());
                System.out.println("Sexo atualizado com sucesso.");
                break;
            case 4:
                System.out.println("Digite a vida:");
                ficha.setVida(lerInteiroPositivo(scanner));
                System.out.println("Vida atualizada com sucesso.");
                break;
            case 5:
                System.out.println("Digite o status:");
                ficha.setStatus(scanner.nextLine().trim());
                System.out.println("Status atualizado com sucesso.");
                break;
            case 6:
                System.out.println("Digite a raca:");
                ficha.setRaca(scanner.nextLine().trim());
                System.out.println("Raca atualizada com sucesso.");
                break;
            case 7:
                System.out.println("Digite a classe:");
                ficha.setClasse(scanner.nextLine().trim());
                System.out.println("Classe atualizada com sucesso.");
                break;
            case 8:
                System.out.println("Digite o nivel:");
                ficha.setNivel(lerInteiroPositivo(scanner));
                System.out.println("Nivel atualizado com sucesso.");
                break;
            case 9:
                System.out.println("Digite a quantidade de moedas de bronze:");
                ficha.setMoedaBronze(lerInteiroPositivo(scanner));
                System.out.println("Moedas de bronze atualizadas com sucesso.");
                break;
            case 10:
                System.out.println("Digite a quantidade de moedas de prata:");
                ficha.setMoedaPrata(lerInteiroPositivo(scanner));
                System.out.println("Moedas de prata atualizadas com sucesso.");
                break;
            case 11:
                System.out.println("Digite a quantidade de moedas de ouro:");
                ficha.setMoedaOuro(lerInteiroPositivo(scanner));
                System.out.println("Moedas de ouro atualizadas com sucesso.");
                break;
            default:
                System.out.println("Opcao invalida.");
        }
    }

    private static int lerInteiroPositivo(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine();
            try {
                int valor = Integer.parseInt(entrada.trim());
                if (valor >= 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // continua no loop
            }
            System.out.println("Entrada invalida. Digite um numero inteiro maior ou igual a zero.");
        }
    }

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[3J\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Nao foi possivel limpar a tela!");
        }
    }

    private static void ajustarTamanhoTerminal() {
        if (System.console() == null) {
            return;
        }

        // ANSI CSI 8 resizes the terminal window in terminals that support it.
        System.out.printf("\033[8;%d;%dt", TERMINAL_ROWS, TERMINAL_COLUMNS);
        System.out.flush();
    }

    private static void abrirMenuHabilidades(Scanner scanner) {
        HabilidadeRepository repository = new HabilidadeRepository();
        HabilidadeService service = new HabilidadeService(repository);
        MenuHabilidades view = new MenuHabilidades(scanner);
        HabilidadesController controller = new HabilidadesController(service, view, App::clear, App::cabecalho);
        controller.iniciar();
    }

    private static void abrirMenuItens(Scanner scanner) {
        clear();
        cabecalho();
        System.out.println("Area de itens");
        System.out.println("Em breve voce podera gerenciar os itens da ficha aqui.");
        System.out.println();
        System.out.println("Pressione Enter para voltar...");
        scanner.nextLine();
        clear();
    }

    private static void abrirMenuEquipamentos(Scanner scanner) {
        clear();
        cabecalho();
        System.out.println("Area de equipamentos");
        System.out.println("Em breve voce podera gerenciar os equipamentos da ficha aqui.");
        System.out.println();
        System.out.println("Pressione Enter para voltar...");
        scanner.nextLine();
        clear();
    }

    private static void abrirMenuAnotacoes(Scanner scanner) {
        clear();
        cabecalho();
        System.out.println("Area de anotacoes");
        System.out.println("Em breve voce podera registrar observacoes da ficha aqui.");
        System.out.println();
        System.out.println("Pressione Enter para voltar...");
        scanner.nextLine();
        clear();
    }
}
