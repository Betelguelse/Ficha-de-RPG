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
                String[] v = linhas.get(i).split(";", -1);
                if (v.length == 9) resultado.add(new Equipamento(v[0], v[1], v[2], v[3], v[4], v[5], inteiro(v[6]), v[7], v[8]));
            }
            return resultado;
        } catch (IOException e) { throw new IllegalStateException("Erro ao ler equipamentos.", e); }
    }

    public void salvar(List<Equipamento> lista) {
        preparar(equipamentos, CABECALHO);
        List<String> linhas = new ArrayList<>(); linhas.add(CABECALHO);
        for (Equipamento e : lista) linhas.add(e.paraLinhaCsv());
        try { Files.write(equipamentos, linhas); } catch (IOException e) { throw new IllegalStateException("Erro ao salvar equipamentos.", e); }
    }

    public boolean[] carregarPermissoes() {
        preparar(permissoes, "leve;media;pesada");
        try {
            List<String> linhas = Files.readAllLines(permissoes);
            if (linhas.size() < 2) return new boolean[] {false, false, false};
            String[] v = linhas.get(1).split(";", -1);
            return new boolean[] {v.length > 0 && Boolean.parseBoolean(v[0]), v.length > 1 && Boolean.parseBoolean(v[1]), v.length > 2 && Boolean.parseBoolean(v[2])};
        } catch (IOException e) { throw new IllegalStateException("Erro ao ler permissoes.", e); }
    }

    public void salvarPermissoes(boolean[] p) {
        preparar(permissoes, "leve;media;pesada");
        try { Files.write(permissoes, List.of("leve;media;pesada", p[0] + ";" + p[1] + ";" + p[2])); } catch (IOException e) { throw new IllegalStateException("Erro ao salvar permissoes.", e); }
    }

    private void preparar(Path arquivo, String cabecalho) {
        try { Files.createDirectories(arquivo.getParent()); if (!Files.exists(arquivo)) Files.write(arquivo, List.of(cabecalho)); } catch (IOException e) { throw new IllegalStateException("Erro ao preparar arquivo.", e); }
    }
    private int inteiro(String valor) { try { return Integer.parseInt(valor); } catch (NumberFormatException e) { return 0; } }
}
