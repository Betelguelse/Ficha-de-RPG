package br.com.ficha.service;

import java.util.List;

import br.com.ficha.model.Item;
import br.com.ficha.repository.ItemRepository;

public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> listarItens() {
        return repository.listar();
    }

    public Item buscarPorIndice(int indice) {
        List<Item> itens = repository.listar();
        validarIndice(indice, itens.size());
        return itens.get(indice);
    }

    public void adicionarItem(Item item) {
        repository.adicionar(item);
    }

    public Item excluirItem(int indice) {
        List<Item> itens = repository.listar();
        validarIndice(indice, itens.size());
        Item removido = itens.remove(indice);
        repository.salvarTodos(itens);
        return removido;
    }

    public void editarItem(int indice, Item item) {
        List<Item> itens = repository.listar();
        validarIndice(indice, itens.size());
        itens.set(indice, item);
        repository.salvarTodos(itens);
    }

    private void validarIndice(int indice, int total) {
        if (indice < 0 || indice >= total) {
            throw new IllegalArgumentException("Indice de item invalido.");
        }
    }
}
