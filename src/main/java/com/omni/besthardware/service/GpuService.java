package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.GpuFiltroRequest;
import com.omni.besthardware.model.GpuModel;
import com.omni.besthardware.repository.GpuRepository;
import com.omni.besthardware.specifications.GpuSpecification;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GpuService extends AbstractCrudService<GpuModel, Integer> {

    private final GpuRepository gpuRepository;

    public GpuService(GpuRepository gpuRepository) {
        super(gpuRepository, "GPU", GpuModel::setId);
        this.gpuRepository = gpuRepository;
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
}
