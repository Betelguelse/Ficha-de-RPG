package br.com.ficha.repository;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import br.com.ficha.model.Ficha;

public class FichaRepository {
    private static final String CABECALHO = "nome;idade;sexo;vidaAtual;status;raca;classe;nivel;moedaBronze;moedaPrata;moedaOuro;inteligencia;sabedoria;forca;destreza;constituicao;carisma;bonusProficiencia;vidaMaxima;vidaTemporaria";
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

            int vidaAtual = parseInteiro(dados[3]);
            int vidaMaxima = dados.length > 18 ? parseInteiro(dados[18]) : vidaAtual;
            int vidaTemporaria = dados.length > 19 ? parseInteiro(dados[19]) : 0;

            return new Ficha(
                dados[0],
                parseInteiro(dados[1]),
                dados[2],
                vidaAtual,
                dados[4],
                dados[5],
                dados[6],
                parseInteiro(dados[7]),
                dados.length > 8 ? parseInteiro(dados[8]) : 0,
                dados.length > 9 ? parseInteiro(dados[9]) : 0,
                dados.length > 10 ? parseInteiro(dados[10]) : 0,
                dados.length > 11 ? parseInteiro(dados[11]) : 0,
                dados.length > 12 ? parseInteiro(dados[12]) : 0,
                dados.length > 13 ? parseInteiro(dados[13]) : 0,
                dados.length > 14 ? parseInteiro(dados[14]) : 0,
                dados.length > 15 ? parseInteiro(dados[15]) : 0,
                dados.length > 16 ? parseInteiro(dados[16]) : 0,
                dados.length > 17 ? parseInteiro(dados[17]) : 0,
                vidaMaxima,
                vidaTemporaria
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
