package com.omni.besthardware.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omni.besthardware.model.FonteModel;
import com.omni.besthardware.repository.FonteRepository;
import com.omni.besthardware.rest.dto.request.FonteFiltroRequest;
import com.omni.besthardware.specifications.FonteSpecification;


@Service
public class FonteService extends AbstractCrudService<FonteModel, Integer> {

    private final FonteRepository fonteRepository;

    public FonteService(FonteRepository fonteRepository) {
        super(fonteRepository, "Fonte", FonteModel::setId);
        this.fonteRepository = fonteRepository;
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarComFiltros(FonteFiltroRequest filtro) {
        return fonteRepository.findAll(FonteSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorTipo(String tipo) {
        return fonteRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return fonteRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorMarca(String marca) {
        return fonteRepository.findByMarcaContainingIgnoreCase(marca);
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorPotenciaMinima(Integer potencia) {
        return fonteRepository.findByPotenciaGreaterThanEqual(potencia);
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorCertificacao(String certificacao) {
        return fonteRepository.findByCertificacaoContainingIgnoreCase(certificacao);
    }
}
