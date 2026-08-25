package br.com.ficha.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.ficha.model.Equipamento;

public class EquipamentoRepository {
    private static final String CABECALHO = "nome;tipo;categoria;dano;tipoDano;peso;valor;raridade;equipadoComo";
    private final Path equipamentos = Paths.get("src/main/resources/equipamentos.csv");
    private final Path permissoes = Paths.get("src/main/resources/permissoes-armadura.csv");

    public List<Equipamento> listar() {
        preparar(equipamentos, CABECALHO);
        List<Equipamento> resultado = new ArrayList<>();
        try {
            List<String> linhas = Files.readAllLines(equipamentos);
            for (int i = 1; i < linhas.size(); i++) {
                String[] valores = linhas.get(i).split(";", -1);
                if (valores.length == 9) {
                    resultado.add(new Equipamento(
                        valores[0],
                        valores[1],
                        valores[2],
                        valores[3],
                        valores[4],
                        valores[5],
                        inteiro(valores[6]),
                        valores[7],
                        valores[8]
                    ));
                }
            }
            return resultado;
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler equipamentos.", e);
        }
    }

    public void salvar(List<Equipamento> lista) {
        preparar(equipamentos, CABECALHO);
        List<String> linhas = new ArrayList<>();
        linhas.add(CABECALHO);
        for (Equipamento equipamento : lista) {
            linhas.add(equipamento.paraLinhaCsv());
        }
        try {
            Files.write(equipamentos, linhas);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar equipamentos.", e);
        }
    }

    public boolean[] carregarPermissoes() {
        preparar(permissoes, "leve;media;pesada");
        try {
            List<String> linhas = Files.readAllLines(permissoes);
            if (linhas.size() < 2) {
                return new boolean[] {false, false, false};
            }
            String[] valores = linhas.get(1).split(";", -1);
            return new boolean[] {
                valores.length > 0 && Boolean.parseBoolean(valores[0]),
                valores.length > 1 && Boolean.parseBoolean(valores[1]),
                valores.length > 2 && Boolean.parseBoolean(valores[2])
            };
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler permissões.", e);
        }
    }

    public void salvarPermissoes(boolean[] permissoesArmadura) {
        preparar(permissoes, "leve;media;pesada");
        String valores = permissoesArmadura[0] + ";"
            + permissoesArmadura[1] + ";"
            + permissoesArmadura[2];
        try {
            Files.write(permissoes, List.of("leve;media;pesada", valores));
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar permissões.", e);
        }
    }

    private void preparar(Path arquivo, String cabecalho) {
        try {
            Files.createDirectories(arquivo.getParent());
            if (!Files.exists(arquivo)) {
                Files.write(arquivo, List.of(cabecalho));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao preparar arquivo.", e);
        }
    }

    private int inteiro(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
