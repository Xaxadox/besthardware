package com.omni.besthardware.repository;

import com.omni.besthardware.model.GpuModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio responsavel pelo acesso aos dados de Gpu.
 */
public interface GpuRepository extends JpaRepository<GpuModel, Integer>, JpaSpecificationExecutor<GpuModel> {
    List<GpuModel> findByTipoIgnoreCase(String tipo);

    List<GpuModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<GpuModel> findByModeloContainingIgnoreCase(String modelo);

    List<GpuModel> findByMarcaContainingIgnoreCase(String marca);

    List<GpuModel> findByMemoriaGreaterThanEqual(Integer memoria);

    List<GpuModel> findByConsumoLessThanEqual(Integer consumo);
}
