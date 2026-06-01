package com.omni.besthardware.services;

import com.omni.besthardware.dtos.CpuFiltroRequest;
import com.omni.besthardware.models.CpuModel;
import com.omni.besthardware.repositories.CpuRepository;
import com.omni.besthardware.specifications.CpuSpecification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CpuService {

    private final CpuRepository cpuRepository;

    public CpuService(CpuRepository cpuRepository) {
        this.cpuRepository = cpuRepository;
    }

    @Transactional(readOnly = true)
    public List<CpuModel> listarTodos() {
        return cpuRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CpuModel> buscarPorId(Integer id) {
        return cpuRepository.findById(id);
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

    @Transactional
    public CpuModel salvar(CpuModel cpu) {
        return cpuRepository.save(cpu);
    }

    @Transactional
    public Optional<CpuModel> atualizar(Integer id, CpuModel cpu) {
        if (!cpuRepository.existsById(id)) {
            return Optional.empty();
        }

        cpu.setId(id);
        return Optional.of(cpuRepository.save(cpu));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!cpuRepository.existsById(id)) {
            return false;
        }

        cpuRepository.deleteById(id);
        return true;
    }
}
