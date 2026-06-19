package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.MonitorFiltroRequest;
import com.omni.besthardware.model.MonitorModel;
import com.omni.besthardware.repository.MonitorRepository;
import com.omni.besthardware.specifications.MonitorSpecification;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MonitorService extends AbstractCrudService<MonitorModel, Integer> {

    private final MonitorRepository monitorRepository;

    public MonitorService(MonitorRepository monitorRepository) {
        super(monitorRepository, "Monitor", MonitorModel::setId);
        this.monitorRepository = monitorRepository;
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> buscarComFiltros(MonitorFiltroRequest filtro) {
        return monitorRepository.findAll(MonitorSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> buscarPorTipo(String tipo) {
        return monitorRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return monitorRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> buscarPorMarca(String marca) {
        return monitorRepository.findByMarcaContainingIgnoreCase(marca);
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> buscarPorTamanho(String tamanho) {
        return monitorRepository.findByTamanho(tamanho);
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> buscarPorResolucao(String resolucao) {
        return monitorRepository.findByResolucaoIgnoreCase(resolucao);
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> buscarPorFrequenciaMinima(Integer frequencia) {
        return monitorRepository.findByFrequenciaGreaterThanEqual(frequencia);
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> buscarPorTecnologia(String tecnologia) {
        return monitorRepository.findByTecnologiaIgnoreCase(tecnologia);
    }
}
