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

    public void atualizarVidaAtual(Ficha ficha, int vidaAtual) {
        validarNaoNegativo(vidaAtual);
        validarLimiteVida(vidaAtual, ficha.getVidaMaxima());
        ficha.setVidaAtual(vidaAtual);
    }

    public void atualizarVidaMaxima(Ficha ficha, int vidaMaxima) {
        validarNaoNegativo(vidaMaxima);
        validarLimiteVida(ficha.getVidaAtual(), vidaMaxima);
        ficha.setVidaMaxima(vidaMaxima);
    }

    public void atualizarVidaTemporaria(Ficha ficha, int vidaTemporaria) {
        validarNaoNegativo(vidaTemporaria);
        ficha.setVidaTemporaria(vidaTemporaria);
    }

    private void validarLimiteVida(int vidaAtual, int vidaMaxima) {
        if (vidaAtual > vidaMaxima) {
            throw new IllegalArgumentException("A vida atual não pode ultrapassar a vida máxima.");
        }
    }

    private void validarNaoNegativo(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("A vida não pode ser negativa.");
        }
    }
}
