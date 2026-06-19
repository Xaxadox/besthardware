package com.omni.besthardware.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omni.besthardware.model.CpuModel;
import com.omni.besthardware.repository.CpuRepository;
import com.omni.besthardware.rest.dto.request.CpuFiltroRequest;
import com.omni.besthardware.specifications.CpuSpecification;


@Service
public class CpuService extends AbstractCrudService<CpuModel, Integer> {

    private final CpuRepository cpuRepository;

    public CpuService(CpuRepository cpuRepository) {
        super(cpuRepository, "CPU", CpuModel::setId);
        this.cpuRepository = cpuRepository;
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarComFiltros(CpuFiltroRequest filtro) {
        return cpuRepository.findAll(CpuSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarPorTipo(String tipo) {
        return cpuRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return cpuRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarPorModelo(String modelo) {
        return cpuRepository.findByModeloContainingIgnoreCase(modelo);
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarPorSocket(String socket) {
        return cpuRepository.findBySocketIgnoreCase(socket);
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarPorFrequenciaMinima(Integer frequencia) {
        return cpuRepository.findByFrequenciaGreaterThanEqual(frequencia);
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarPorConsumoMaximo(Integer consumo) {
        return cpuRepository.findByConsumoLessThanEqual(consumo);
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarPorNucleosMinimos(Integer nucleos) {
        return cpuRepository.findByNucleosGreaterThanEqual(nucleos);
    }

    @Transactional(readOnly = true)
    public List<CpuModel> buscarPorPeriodoLancamento(LocalDate dataInicial, LocalDate dataFinal) {
        return cpuRepository.findByAnoLancamentoBetween(dataInicial, dataFinal);
    }
}
