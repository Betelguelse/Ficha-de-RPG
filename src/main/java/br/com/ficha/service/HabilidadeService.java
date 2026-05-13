package br.com.ficha.service;

import java.util.List;

import br.com.ficha.model.Habilidade;
import br.com.ficha.repository.HabilidadeRepository;

public class HabilidadeService {
    private final HabilidadeRepository repository;

    public HabilidadeService(HabilidadeRepository repository) {
        this.repository = repository;
    }

    public List<Habilidade> listarHabilidades() {
        return repository.listar();
    }

    public Habilidade buscarPorIndice(int indice) {
        List<Habilidade> habilidades = repository.listar();
        validarIndice(indice, habilidades.size());
        return habilidades.get(indice);
    }

    public void adicionarHabilidade(Habilidade habilidade) {
        validarNomeDuplicado(habilidade.getNome());
        repository.adicionar(habilidade);
    }

    public Habilidade excluirHabilidade(int indice) {
        List<Habilidade> habilidades = repository.listar();
        validarIndice(indice, habilidades.size());
        Habilidade removida = habilidades.remove(indice);
        repository.salvarTodos(habilidades);
        return removida;
    }

    private void validarIndice(int indice, int total) {
        if (indice < 0 || indice >= total) {
            throw new IllegalArgumentException("Indice de habilidade invalido.");
        }
    }

    private void validarNomeDuplicado(String nome) {
        for (Habilidade habilidade : repository.listar()) {
            if (habilidade.getNome().equalsIgnoreCase(nome)) {
                throw new IllegalArgumentException("Ja existe uma habilidade com esse nome.");
            }
        }
    }
}
