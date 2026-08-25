package br.com.ficha.service;

import java.util.List;
import br.com.ficha.model.Anotacao;
import br.com.ficha.repository.AnotacaoRepository;

public class AnotacaoService {

    private final AnotacaoRepository repository;

    public AnotacaoService(AnotacaoRepository repository) { 
        this.repository = repository; 
    }

    public List<Anotacao> listar() {
        return repository.listar(); 
    }
    
    public void salvar(List<Anotacao> anotacoes) { 
        repository.salvar(anotacoes); 
    }
}
