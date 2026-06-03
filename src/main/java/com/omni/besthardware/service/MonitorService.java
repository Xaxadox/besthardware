package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.MonitorFiltroRequest;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.MonitorModel;
import com.omni.besthardware.repository.MonitorRepository;
import com.omni.besthardware.specifications.MonitorSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico responsavel pelas regras de negocio de Monitor no projeto BestHardware.
 */
@Service
public class MonitorService {

    private final MonitorRepository monitorRepository;

    public MonitorService(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    @Transactional(readOnly = true)
    public List<MonitorModel> listarTodos() {
        return monitorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MonitorModel> buscarPorId(Integer id) {
        return monitorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public MonitorModel buscarObrigatorio(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Monitor", id));
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

    @Transactional
    public MonitorModel salvar(MonitorModel monitor) {
        return monitorRepository.save(monitor);
    }

    @Transactional
    public Optional<MonitorModel> atualizar(Integer id, MonitorModel monitor) {
        if (!monitorRepository.existsById(id)) {
            return Optional.empty();
        }

        monitor.setId(id);
        return Optional.of(monitorRepository.save(monitor));
    }

    @Transactional
    public MonitorModel atualizarObrigatorio(Integer id, MonitorModel monitor) {
        if (!monitorRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Monitor", id);
        }

        monitor.setId(id);
        return monitorRepository.save(monitor);
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!monitorRepository.existsById(id)) {
            return false;
        }

        monitorRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Monitor", id);
        }
    }
}
