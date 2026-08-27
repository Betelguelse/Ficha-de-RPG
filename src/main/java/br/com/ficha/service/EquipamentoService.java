package br.com.ficha.service;

import java.util.List;

import br.com.ficha.model.Equipamento;
import br.com.ficha.repository.EquipamentoRepository;

public class EquipamentoService {
    private final EquipamentoRepository repository;

    public EquipamentoService(EquipamentoRepository repository) {
        this.repository = repository;
    }

    public List<Equipamento> listar() {
        return repository.listar();
    }

    public Equipamento buscarPorIndice(int indice) {
        List<Equipamento> equipamentos = repository.listar();
        validarIndice(indice, equipamentos.size());
        return equipamentos.get(indice);
    }

    public void adicionar(Equipamento equipamento) {
        List<Equipamento> equipamentos = repository.listar();
        equipamentos.add(equipamento);
        repository.salvar(equipamentos);
    }

    public void editar(int indice, Equipamento equipamento) {
        List<Equipamento> equipamentos = repository.listar();
        validarIndice(indice, equipamentos.size());
        if ("Armadura".equals(equipamento.getEquipadoComo())) {
            if (!"Armadura".equals(equipamento.getTipo())) {
                throw new IllegalArgumentException("Um equipamento no slot Armadura deve ser uma armadura.");
            }
            validarPermissaoArmadura(equipamento.getCategoria());
        }
        equipamentos.set(indice, equipamento);
        repository.salvar(equipamentos);
    }

    public String alternarEquipado(int indice, String slotArma) {
        List<Equipamento> equipamentos = repository.listar();
        validarIndice(indice, equipamentos.size());
        Equipamento escolhido = equipamentos.get(indice);

        if (!escolhido.getEquipadoComo().isEmpty()) {
            escolhido.setEquipadoComo("");
            repository.salvar(equipamentos);
            return "Equipamento desequipado.";
        }

        String slot = determinarSlot(escolhido, slotArma);
        for (Equipamento equipamento : equipamentos) {
            if (equipamento.getEquipadoComo().equals(slot)) {
                throw new IllegalArgumentException(
                    "Já existe equipamento no slot " + slot + ". Desequipe-o primeiro."
                );
            }
        }

        escolhido.setEquipadoComo(slot);
        repository.salvar(equipamentos);
        return "Equipamento equipado como " + slot + ".";
    }

    public boolean[] carregarPermissoes() {
        return repository.carregarPermissoes();
    }

    public void salvarPermissoes(boolean[] permissoes) {
        for (Equipamento equipamento : repository.listar()) {
            if ("Armadura".equals(equipamento.getEquipadoComo())
                && !permissoes[indiceCategoriaArmadura(equipamento.getCategoria())]) {
                throw new IllegalArgumentException(
                    "Desequipe a armadura " + equipamento.getNome()
                        + " antes de remover a permissão da categoria " + equipamento.getCategoria() + "."
                );
            }
        }
        repository.salvarPermissoes(permissoes);
    }

    private String determinarSlot(Equipamento equipamento, String slotArma) {
        if (equipamento.getTipo().equals("Arma")) {
            if (!"Principal".equals(slotArma) && !"Secundária".equals(slotArma)) {
                throw new IllegalArgumentException("Escolha um slot válido para a arma.");
            }
            return slotArma;
        }

        if (equipamento.getTipo().equals("Armadura")) {
            validarPermissaoArmadura(equipamento.getCategoria());
            return "Armadura";
        }

        throw new IllegalArgumentException("Apenas armas e armaduras podem ser equipadas.");
    }

    private void validarPermissaoArmadura(String categoria) {
        int indice = indiceCategoriaArmadura(categoria);

        if (!repository.carregarPermissoes()[indice]) {
            throw new IllegalArgumentException("Esta categoria de armadura não está permitida.");
        }
    }

    private int indiceCategoriaArmadura(String categoria) {
        return switch (categoria) {
            case "Leve" -> 0;
            case "Média", "Media" -> 1;
            case "Pesada" -> 2;
            default -> throw new IllegalArgumentException("Categoria de armadura inválida.");
        };
    }

    private void validarIndice(int indice, int total) {
        if (indice < 0 || indice >= total) {
            throw new IllegalArgumentException("Índice de equipamento inválido.");
        }
    }
}
