package com.omni.besthardware.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omni.besthardware.model.ArmazenamentoModel;
import com.omni.besthardware.repository.ArmazenamentoRepository;
import com.omni.besthardware.rest.dto.request.ArmazenamentoFiltroRequest;
import com.omni.besthardware.specifications.ArmazenamentoSpecification;

@Service
public class ArmazenamentoService extends AbstractCrudService<ArmazenamentoModel, Integer> {

    private final ArmazenamentoRepository armazenamentoRepository;

    public ArmazenamentoService(ArmazenamentoRepository armazenamentoRepository) {
        super(armazenamentoRepository, "Armazenamento", ArmazenamentoModel::setId);
        this.armazenamentoRepository = armazenamentoRepository;
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
}
