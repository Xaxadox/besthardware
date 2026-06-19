package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.RamFiltroRequest;
import com.omni.besthardware.model.RamModel;
import com.omni.besthardware.repository.RamRepository;
import com.omni.besthardware.specifications.RamSpecification;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RamService extends AbstractCrudService<RamModel, Integer> {

    private final RamRepository ramRepository;

    public RamService(RamRepository ramRepository) {
        super(ramRepository, "RAM", RamModel::setId);
        this.ramRepository = ramRepository;
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarComFiltros(RamFiltroRequest filtro) {
        return ramRepository.findAll(RamSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorTipo(String tipo) {
        return ramRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return ramRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorGeracao(String geracao) {
        return ramRepository.findByGeracaoIgnoreCase(geracao);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorFrequenciaMinima(Integer frequencia) {
        return ramRepository.findByFrequenciaGreaterThanEqual(frequencia);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorMarca(String marca) {
        return ramRepository.findByMarcaContainingIgnoreCase(marca);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorMemoriaMinima(Integer memoria) {
        return ramRepository.findByMemoriaGreaterThanEqual(memoria);
    }
}
