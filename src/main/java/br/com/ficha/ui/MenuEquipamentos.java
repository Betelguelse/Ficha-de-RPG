package br.com.ficha.ui;

import java.util.List;
import java.util.Scanner;

import br.com.ficha.model.Equipamento;

public class MenuEquipamentos {
    private static final String[] RARIDADES = {
        "Comum", "Incomum", "Raro", "Muito Raro", "Lendário"
    };

    private final Scanner scanner;

    public MenuEquipamentos(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibirMenu() {
        System.out.println("EQUIPAMENTOS");
        System.out.println("1 - Exibir equipamentos");
        System.out.println("2 - Adicionar equipamento");
        System.out.println("3 - Equipar ou desequipar");
        System.out.println("4 - Configurar permissões de armadura");
        System.out.println("5 - Editar equipamento");
        System.out.println("0 - Voltar");
    }

    public void exibirLista(List<Equipamento> equipamentos) {
        if (equipamentos.isEmpty()) {
            System.out.println("Nenhum equipamento encontrado.");
            return;
        }

        for (int i = 0; i < equipamentos.size(); i++) {
            Equipamento equipamento = equipamentos.get(i);
            String slot = equipamento.getEquipadoComo().isEmpty()
                ? ""
                : " [" + equipamento.getEquipadoComo() + "]";
            System.out.println((i + 1) + " - " + equipamento.getNome() + slot);
        }
    }

    public void exibirDetalhes(Equipamento equipamento) {
        System.out.println("\n" + equipamento.getNome());
        System.out.println("Tipo: " + equipamento.getTipo());
        System.out.println("Categoria: " + equipamento.getCategoria());
        System.out.println("Dano: " + equipamento.getDano());
        System.out.println("Tipo de dano: " + equipamento.getTipoDano());
        System.out.println("Peso: " + equipamento.getPeso() + " kg");
        System.out.println("Valor: " + equipamento.getValor() + " moedas");
        System.out.println("Raridade: " + equipamento.getRaridade());
        System.out.println(
            "Equipado: " + (equipamento.getEquipadoComo().isEmpty()
                ? "Não"
                : "Sim (" + equipamento.getEquipadoComo() + ")")
        );
    }

    public Equipamento lerNovoEquipamento() {
        System.out.println("ADICIONAR EQUIPAMENTO");
        String nome = lerTexto("Nome (Esc para cancelar):");
        if (nome == null) return null;

        int tipoOpcao = lerOpcaoIntervalo(
            "Tipo: 1 - Arma | 2 - Armadura | 3 - Outro | Esc - Cancelar",
            1,
            3,
            true
        );
        if (tipoOpcao == 0) return null;
        String tipo = tipoOpcao == 1 ? "Arma" : tipoOpcao == 2 ? "Armadura" : "Outro";

        String categoria;
        if (tipo.equals("Armadura")) {
            int opcao = lerOpcaoIntervalo(
                "Categoria: 1 - Leve | 2 - Média | 3 - Pesada | Esc - Cancelar",
                1,
                3,
                true
            );
            if (opcao == 0) return null;
            categoria = opcao == 1 ? "Leve" : opcao == 2 ? "Média" : "Pesada";
        } else {
            categoria = lerTexto("Categoria (Esc para cancelar):");
            if (categoria == null) return null;
        }

        String dano = lerTexto("Dano (use N/A se não se aplicar, Esc para cancelar):");
        if (dano == null) return null;
        String tipoDano = lerTexto("Tipo de dano (use N/A se não se aplicar, Esc para cancelar):");
        if (tipoDano == null) return null;
        String peso = lerTexto("Peso em kg (Esc para cancelar):");
        if (peso == null) return null;
        int valor = lerInteiroPositivo("Valor em moedas (Esc para cancelar):", true);
        if (valor == 0) return null;

        int raridade = lerOpcaoIntervalo(
            "Raridade: 1 - Comum | 2 - Incomum | 3 - Raro | 4 - Muito Raro | 5 - Lendário | Esc - Cancelar",
            1,
            5,
            true
        );
        if (raridade == 0) return null;

        return new Equipamento(
            nome,
            tipo,
            categoria,
            dano,
            tipoDano,
            peso,
            valor,
            RARIDADES[raridade - 1],
            ""
        );
    }

    public Equipamento lerEdicao(Equipamento atual) {
        exibirDetalhes(atual);
        int campo = lerOpcaoIntervalo(
            "\nCampo: 1 - Nome | 2 - Categoria | 3 - Dano | 4 - Tipo de dano | 5 - Peso | 6 - Valor | 7 - Raridade",
            1,
            7,
            false
        );

        Equipamento editado = copiar(atual);
        if (campo == 6) {
            editado.setValor(lerInteiroPositivo("Valor atual: " + atual.getValor() + "\nNovo valor:", false));
            return editado;
        }

        String antigo = valorDoCampo(atual, campo);
        String valor = lerTexto("Valor atual: " + antigo + "\nNovo valor (Esc para cancelar):");
        if (valor == null) return null;

        switch (campo) {
            case 1 -> editado.setNome(valor);
            case 2 -> editado.setCategoria(valor);
            case 3 -> editado.setDano(valor);
            case 4 -> editado.setTipoDano(valor);
            case 5 -> editado.setPeso(valor);
            case 7 -> editado.setRaridade(valor);
            default -> throw new IllegalStateException("Campo de edição inválido.");
        }
        return editado;
    }

    public boolean[] lerPermissoes(boolean[] atuais) {
        String[] categorias = {"Leve", "Média", "Pesada"};
        boolean[] novas = atuais.clone();
        for (int i = 0; i < categorias.length; i++) {
            int opcao = lerOpcaoIntervalo(
                "Permitir armadura " + categorias[i] + "? (1 - Sim | 2 - Não)",
                1,
                2,
                false
            );
            novas[i] = opcao == 1;
        }
        return novas;
    }

    public String lerSlotArma() {
        int opcao = lerOpcaoIntervalo("1 - Principal | 2 - Secundária", 1, 2, false);
        return opcao == 1 ? "Principal" : "Secundária";
    }

    public int lerOpcao() {
        return lerInteiro();
    }

    public int lerIndice(String mensagem) {
        System.out.println(mensagem);
        return lerInteiro();
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void aguardarEnter() {
        scanner.nextLine();
    }

    private Equipamento copiar(Equipamento equipamento) {
        return new Equipamento(
            equipamento.getNome(),
            equipamento.getTipo(),
            equipamento.getCategoria(),
            equipamento.getDano(),
            equipamento.getTipoDano(),
            equipamento.getPeso(),
            equipamento.getValor(),
            equipamento.getRaridade(),
            equipamento.getEquipadoComo()
        );
    }

    private String valorDoCampo(Equipamento equipamento, int campo) {
        return switch (campo) {
            case 1 -> equipamento.getNome();
            case 2 -> equipamento.getCategoria();
            case 3 -> equipamento.getDano();
            case 4 -> equipamento.getTipoDano();
            case 5 -> equipamento.getPeso();
            case 7 -> equipamento.getRaridade();
            default -> "";
        };
    }

    private String lerTexto(String mensagem) {
        System.out.println(mensagem);
        while (true) {
            String entrada = scanner.nextLine();
            if (entrada.equals("\u001B")) return null;
            entrada = entrada.trim();
            if (!entrada.isEmpty()) return entrada;
            System.out.println("Entrada inválida. Digite um texto.");
        }
    }

    private int lerInteiroPositivo(String mensagem, boolean permitirCancelar) {
        System.out.println(mensagem);
        while (true) {
            int valor = lerInteiro();
            if (permitirCancelar && valor == Integer.MIN_VALUE) return 0;
            if (valor > 0) return valor;
            System.out.println("Entrada inválida. Digite um número inteiro positivo.");
        }
    }

    private int lerOpcaoIntervalo(String mensagem, int minimo, int maximo, boolean permitirCancelar) {
        System.out.println(mensagem);
        while (true) {
            int opcao = lerInteiro();
            if (permitirCancelar && opcao == Integer.MIN_VALUE) return 0;
            if (opcao >= minimo && opcao <= maximo) return opcao;
            System.out.println("Opção inválida.");
        }
    }

    private int lerInteiro() {
        while (true) {
            String entrada = scanner.nextLine();
            if (entrada.equals("\u001B")) return Integer.MIN_VALUE;
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
            }
        }
    }
}
