package br.com.ficha.service;

import br.com.ficha.model.Ficha;
import br.com.ficha.repository.FichaRepository;

public class FichaService {
    private final FichaRepository repository;

    public FichaService(FichaRepository repository) {
        this.repository = repository;
    }

    public Ficha carregarFicha() {
        return repository.carregar();
    }

    public void salvarFicha(Ficha ficha) {
        repository.salvar(ficha);
    }
}
