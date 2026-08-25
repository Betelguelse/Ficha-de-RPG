package br.com.ficha.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import br.com.ficha.model.Item;

public class ItemRepository {
    private static final String CABECALHO = "nome;quantidade;tipo;descricao";
    private final Path caminhoArquivo;

    public ItemRepository() {
        this(Paths.get("src/main/resources/itens.csv"));
    }

    public ItemRepository(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public List<Item> listar() {
        garantirArquivo();
        List<Item> itens = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(caminhoArquivo)) {
            reader.readLine();
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] valores = linha.split(";", -1);
                if (valores.length == 4) {
                    itens.add(new Item(valores[0], parseQuantidade(valores[1]), valores[2], valores[3]));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler itens: " + e.getMessage(), e);
        }

        return itens;
    }

    public void adicionar(Item item) {
        List<Item> itens = listar();
        itens.add(item);
        salvarTodos(itens);
    }

    public void salvarTodos(List<Item> itens) {
        garantirArquivo();

        try (BufferedWriter writer = Files.newBufferedWriter(caminhoArquivo)) {
            writer.write(CABECALHO);
            writer.newLine();
            for (Item item : itens) {
                writer.write(item.paraLinhaCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao salvar itens: " + e.getMessage(), e);
        }
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
            throw new IllegalStateException("Erro ao preparar arquivo de itens: " + e.getMessage(), e);
        }
    }

    private int parseQuantidade(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
