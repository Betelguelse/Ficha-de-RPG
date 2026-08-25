package br.com.ficha.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.ficha.model.Anotacao;

public class AnotacaoRepository {
    private final Path caminhoArquivo = Paths.get("src/main/resources/anotacoes.csv");

    public List<Anotacao> listar() {
        try {
            garantirArquivo();
            List<String> linhas = Files.readAllLines(caminhoArquivo);
            List<Anotacao> anotacoes = new ArrayList<>();

            for (int i = 1; i < linhas.size(); i++) {
                String[] valores = linhas.get(i).split(";", -1);
                if (valores.length == 2) {
                    anotacoes.add(new Anotacao(valores[0], valores[1]));
                }
            }

            return anotacoes;
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler anotações.", e);
        }
    }

    public void salvar(List<Anotacao> anotacoes) {
        try {
            garantirArquivo();
            List<String> linhas = new ArrayList<>();
            linhas.add("titulo;texto");

            for (Anotacao anotacao : anotacoes) {
                linhas.add(anotacao.csv());
            }

            Files.write(caminhoArquivo, linhas);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar anotações.", e);
        }
    }

    private void garantirArquivo() throws IOException {
        Files.createDirectories(caminhoArquivo.getParent());
        if (!Files.exists(caminhoArquivo)) {
            Files.write(caminhoArquivo, List.of("titulo;texto"));
        }
    }
}
