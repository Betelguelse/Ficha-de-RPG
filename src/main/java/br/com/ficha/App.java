package br.com.ficha;

import java.util.Scanner;

import br.com.ficha.controller.AnotacoesController;
import br.com.ficha.controller.AtributosController;
import br.com.ficha.controller.DadosPersonagemController;
import br.com.ficha.controller.EquipamentosController;
import br.com.ficha.controller.HabilidadesController;
import br.com.ficha.controller.ItensController;
import br.com.ficha.repository.AnotacaoRepository;
import br.com.ficha.repository.EquipamentoRepository;
import br.com.ficha.repository.FichaRepository;
import br.com.ficha.repository.HabilidadeRepository;
import br.com.ficha.repository.ItemRepository;
import br.com.ficha.service.AnotacaoService;
import br.com.ficha.service.EquipamentoService;
import br.com.ficha.service.FichaService;
import br.com.ficha.service.HabilidadeService;
import br.com.ficha.service.ItemService;
import br.com.ficha.ui.MenuAnotacoes;
import br.com.ficha.ui.MenuAtributos;
import br.com.ficha.ui.MenuDadosPersonagem;
import br.com.ficha.ui.MenuEquipamentos;
import br.com.ficha.ui.MenuHabilidades;
import br.com.ficha.ui.MenuItens;
import br.com.ficha.ui.MenuPrincipal;

public class App {
    private static final int TERMINAL_ROWS = 20;
    private static final int TERMINAL_COLUMNS = 72;
    private static final Scanner scanner = new Scanner(System.in);
    private static final FichaService fichaService = new FichaService(new FichaRepository());

    public static void main(String[] args) {
        ajustarTamanhoTerminal();
        iniciar(scanner);
        System.out.println("Saindo do sistema. Até mais!");
        scanner.close();
    }

    public static void iniciar(Scanner scanner) {
        MenuPrincipal menuPrincipal = new MenuPrincipal(scanner);
        MenuDadosPersonagem menuDados = new MenuDadosPersonagem(scanner);
        boolean continuar = true;

        clear();
        while (continuar) {
            clear();
            cabecalho();
            menuDados.exibirDados(fichaService.carregarFicha());
            menuPrincipal.exibir();

            switch (menuPrincipal.lerOpcao()) {
                case 1 -> abrirDadosPersonagem(scanner);
                case 2 -> abrirAtributos(scanner);
                case 3 -> abrirItens(scanner);
                case 4 -> abrirEquipamentos(scanner);
                case 5 -> abrirHabilidades(scanner);
                case 6 -> abrirAnotacoes(scanner);
                case 0 -> continuar = false;
                default -> menuPrincipal.exibirMensagem("Opção inválida. Tente novamente.");
            }
        }
        clear();
    }

    public static void cabecalho() {
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
        System.out.println("            Bem-vindo ao sistema de ficha");
        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
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
            System.out.println("Não foi possível limpar a tela!");
        }
    }

    private static void ajustarTamanhoTerminal() {
        if (System.console() == null) return;
        System.out.printf("\033[8;%d;%dt", TERMINAL_ROWS, TERMINAL_COLUMNS);
        System.out.flush();
    }

    private static void abrirDadosPersonagem(Scanner scanner) {
        DadosPersonagemController controller = new DadosPersonagemController(
            fichaService,
            new MenuDadosPersonagem(scanner),
            App::clear,
            App::cabecalho
        );
        controller.iniciar();
    }

    private static void abrirAtributos(Scanner scanner) {
        AtributosController controller = new AtributosController(
            fichaService,
            new MenuAtributos(scanner),
            App::clear,
            App::cabecalho
        );
        controller.iniciar();
    }

    private static void abrirItens(Scanner scanner) {
        ItensController controller = new ItensController(
            new ItemService(new ItemRepository()),
            new MenuItens(scanner),
            App::clear,
            App::cabecalho
        );
        controller.iniciar();
    }

    private static void abrirEquipamentos(Scanner scanner) {
        EquipamentosController controller = new EquipamentosController(
            new EquipamentoService(new EquipamentoRepository()),
            new MenuEquipamentos(scanner),
            App::clear,
            App::cabecalho
        );
        controller.iniciar();
    }

    private static void abrirHabilidades(Scanner scanner) {
        HabilidadesController controller = new HabilidadesController(
            new HabilidadeService(new HabilidadeRepository()),
            new MenuHabilidades(scanner),
            App::clear,
            App::cabecalho
        );
        controller.iniciar();
    }

    private static void abrirAnotacoes(Scanner scanner) {
        AnotacoesController controller = new AnotacoesController(
            new AnotacaoService(new AnotacaoRepository()),
            new MenuAnotacoes(scanner),
            App::clear,
            App::cabecalho
        );
        controller.iniciar();
    }
}
