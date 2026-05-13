package br.com.ficha.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.ficha.model.Habilidade;

public class HabilidadeRepository {
    private static final String CABECALHO = "nome;nivel;tipo;recarga;custo;descricao";
    private final Path caminhoArquivo;

    public HabilidadeRepository() {
        this(Paths.get("src/main/resources/habilidades.csv"));
    }

    public HabilidadeRepository(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public List<Habilidade> listar() {
        garantirArquivo();
        List<Habilidade> habilidades = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(caminhoArquivo)) {
            String linha = reader.readLine();
            if (linha == null) {
                return habilidades;
            }

            while ((linha = reader.readLine()) != null) {
                String[] valores = linha.split(";", -1);
                if (valores.length == 6) {
                    habilidades.add(new Habilidade(
                        valores[0],
                        Integer.parseInt(valores[1]),
                        valores[2],
                        valores[3],
                        valores[4],
                        valores[5]
                    ));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler habilidades: " + e.getMessage(), e);
        }

        return habilidades;
    }

    public void salvarTodos(List<Habilidade> habilidades) {
        garantirArquivo();

        try (BufferedWriter writer = Files.newBufferedWriter(caminhoArquivo)) {
            writer.write(CABECALHO);
            writer.newLine();

            for (Habilidade habilidade : habilidades) {
                writer.write(habilidade.paraLinhaCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar habilidades: " + e.getMessage(), e);
        }
    }

    public void adicionar(Habilidade habilidade) {
        List<Habilidade> habilidades = listar();
        habilidades.add(habilidade);
        salvarTodos(habilidades);
    }

    private void garantirArquivo() {
        try {
            Path diretorio = caminhoArquivo.getParent();
            if (diretorio != null) {
                Files.createDirectories(diretorio);
            }
            if (!Files.exists(caminhoArquivo)) {
                Files.writeString(caminhoArquivo, CABECALHO + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao preparar arquivo de habilidades: " + e.getMessage(), e);
        }
    }
}
