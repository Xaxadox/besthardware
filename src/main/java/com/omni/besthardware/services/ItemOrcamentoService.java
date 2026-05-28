package com.omni.besthardware.services;

import com.omni.besthardware.models.ItemOrcamentoModel;
import com.omni.besthardware.repositories.ItemOrcamentoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemOrcamentoService {

    private final ItemOrcamentoRepository itemOrcamentoRepository;

    public ItemOrcamentoService(ItemOrcamentoRepository itemOrcamentoRepository) {
        this.itemOrcamentoRepository = itemOrcamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> listarTodos() {
        return itemOrcamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ItemOrcamentoModel> buscarPorId(Integer id) {
        return itemOrcamentoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorOrcamento(Integer orcamentoId) {
        return itemOrcamentoRepository.findByOrcamentoId(orcamentoId);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorComponente(Integer componenteId) {
        return itemOrcamentoRepository.findByComponenteId(componenteId);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorOrcamentoEComponente(Integer orcamentoId, Integer componenteId) {
        return itemOrcamentoRepository.findByOrcamentoIdAndComponenteId(orcamentoId, componenteId);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorQuantidadeMinima(Integer quantidade) {
        return itemOrcamentoRepository.findByQuantidadeGreaterThanEqual(quantidade);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return itemOrcamentoRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional
    public ItemOrcamentoModel salvar(ItemOrcamentoModel itemOrcamento) {
        return itemOrcamentoRepository.save(itemOrcamento);
    }

    @Transactional
    public Optional<ItemOrcamentoModel> atualizar(Integer id, ItemOrcamentoModel itemOrcamento) {
        if (!itemOrcamentoRepository.existsById(id)) {
            return Optional.empty();
        }

        itemOrcamento.setId(id);
        return Optional.of(itemOrcamentoRepository.save(itemOrcamento));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!itemOrcamentoRepository.existsById(id)) {
            return false;
        }

        itemOrcamentoRepository.deleteById(id);
        return true;
    }
}
