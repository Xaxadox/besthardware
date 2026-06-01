package com.omni.besthardware.services;

import com.omni.besthardware.dtos.GpuFiltroRequest;
import com.omni.besthardware.models.GpuModel;
import com.omni.besthardware.repositories.GpuRepository;
import com.omni.besthardware.specifications.GpuSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GpuService {

    private final GpuRepository gpuRepository;

    public GpuService(GpuRepository gpuRepository) {
        this.gpuRepository = gpuRepository;
    }

    @Transactional(readOnly = true)
    public List<GpuModel> listarTodos() {
        return gpuRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<GpuModel> buscarPorId(Integer id) {
        return gpuRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<GpuModel> buscarComFiltros(GpuFiltroRequest filtro) {
        return gpuRepository.findAll(GpuSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<GpuModel> buscarPorTipo(String tipo) {
        return gpuRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<GpuModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return gpuRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<GpuModel> buscarPorModelo(String modelo) {
        return gpuRepository.findByModeloContainingIgnoreCase(modelo);
    }

    @Transactional(readOnly = true)
    public List<GpuModel> buscarPorMarca(String marca) {
        return gpuRepository.findByMarcaContainingIgnoreCase(marca);
    }

    @Transactional(readOnly = true)
    public List<GpuModel> buscarPorMemoriaMinima(Integer memoria) {
        return gpuRepository.findByMemoriaGreaterThanEqual(memoria);
    }

    @Transactional(readOnly = true)
    public List<GpuModel> buscarPorConsumoMaximo(Integer consumo) {
        return gpuRepository.findByConsumoLessThanEqual(consumo);
    }

    @Transactional
    public GpuModel salvar(GpuModel gpu) {
        return gpuRepository.save(gpu);
    }

    @Transactional
    public Optional<GpuModel> atualizar(Integer id, GpuModel gpu) {
        if (!gpuRepository.existsById(id)) {
            return Optional.empty();
        }

        gpu.setId(id);
        return Optional.of(gpuRepository.save(gpu));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!gpuRepository.existsById(id)) {
            return false;
        }

        gpuRepository.deleteById(id);
        return true;
    }
}
