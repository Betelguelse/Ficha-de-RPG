package br.com.ficha.repository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import br.com.ficha.model.Ficha;

public class FichaRepository {
    private static final String CABECALHO = "nome;idade;sexo;vida;status;raca;classe;nivel;moedaBronze;moedaPrata;moedaOuro";
    private final Path caminhoArquivo;

    public FichaRepository() {
        this(Paths.get("src/main/resources/ficha.csv"));
    }

    public FichaRepository(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        prepararArquivo();
    }

    public Ficha carregar() {
        try {
            List<String> linhas = Files.readAllLines(caminhoArquivo);

            if (linhas.size() < 2) {
                return Ficha.padrao();
            }

            String[] dados = linhas.get(1).split(";", -1);
            if (dados.length < 8) {
                return Ficha.padrao();
            }

            return new Ficha(
                dados[0],
                parseInteiro(dados[1]),
                dados[2],
                parseInteiro(dados[3]),
                dados[4],
                dados[5],
                dados[6],
                parseInteiro(dados[7]),
                dados.length > 8 ? parseInteiro(dados[8]) : 0,
                dados.length > 9 ? parseInteiro(dados[9]) : 0,
                dados.length > 10 ? parseInteiro(dados[10]) : 0
            );
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler ficha: " + e.getMessage(), e);
        }
    }

    public void salvar(Ficha ficha) {
        try (BufferedWriter writer = Files.newBufferedWriter(caminhoArquivo)) {
            writer.write(CABECALHO);
            writer.newLine();
            writer.write(ficha.paraLinhaCsv());
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar ficha: " + e.getMessage(), e);
        }
    }

    private void prepararArquivo() {
        try {
            Path diretorio = caminhoArquivo.getParent();
            if (diretorio != null) {
                Files.createDirectories(diretorio);
            }

            if (!Files.exists(caminhoArquivo)) {
                try (BufferedWriter writer = Files.newBufferedWriter(caminhoArquivo)) {
                    writer.write(CABECALHO);
                    writer.newLine();
                    writer.write(Ficha.padrao().paraLinhaCsv());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao preparar arquivo da ficha: " + e.getMessage(), e);
        }
    }

    private int parseInteiro(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
