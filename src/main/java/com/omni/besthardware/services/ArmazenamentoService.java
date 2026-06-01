package com.omni.besthardware.services;

import com.omni.besthardware.dtos.ArmazenamentoFiltroRequest;
import com.omni.besthardware.models.ArmazenamentoModel;
import com.omni.besthardware.repositories.ArmazenamentoRepository;
import com.omni.besthardware.specifications.ArmazenamentoSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArmazenamentoService {

    private final ArmazenamentoRepository armazenamentoRepository;

    public ArmazenamentoService(ArmazenamentoRepository armazenamentoRepository) {
        this.armazenamentoRepository = armazenamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> listarTodos() {
        return armazenamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ArmazenamentoModel> buscarPorId(Integer id) {
        return armazenamentoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> buscarComFiltros(ArmazenamentoFiltroRequest filtro) {
        return armazenamentoRepository.findAll(ArmazenamentoSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> buscarPorTipo(String tipo) {
        return armazenamentoRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return armazenamentoRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> buscarPorTecnologia(String tecnologia) {
        return armazenamentoRepository.findByTecnologiaIgnoreCase(tecnologia);
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> buscarPorPadrao(String padrao) {
        return armazenamentoRepository.findByPadraoIgnoreCase(padrao);
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> buscarPorMemoriaMinima(Integer memoria) {
        return armazenamentoRepository.findByMemoriaGreaterThanEqual(memoria);
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> buscarPorVelocidadeLeituraMinima(Integer velocidadeLeitura) {
        return armazenamentoRepository.findByVelocidadeLeituraGreaterThanEqual(velocidadeLeitura);
    }

    @Transactional(readOnly = true)
    public List<ArmazenamentoModel> buscarPorVelocidadeEscritaMinima(Integer velocidadeEscrita) {
        return armazenamentoRepository.findByVelocidadeEscritaGreaterThanEqual(velocidadeEscrita);
    }

    @Transactional
    public ArmazenamentoModel salvar(ArmazenamentoModel armazenamento) {
        return armazenamentoRepository.save(armazenamento);
    }

    @Transactional
    public Optional<ArmazenamentoModel> atualizar(Integer id, ArmazenamentoModel armazenamento) {
        if (!armazenamentoRepository.existsById(id)) {
            return Optional.empty();
        }

        armazenamento.setId(id);
        return Optional.of(armazenamentoRepository.save(armazenamento));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!armazenamentoRepository.existsById(id)) {
            return false;
        }

        armazenamentoRepository.deleteById(id);
        return true;
    }
}
