package com.omni.besthardware.repositories;

import com.omni.besthardware.models.GpuModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpuRepository extends JpaRepository<GpuModel, Integer> {
    List<GpuModel> findByTipoIgnoreCase(String tipo);

    List<GpuModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<GpuModel> findByModeloContainingIgnoreCase(String modelo);

    List<GpuModel> findByMarcaContainingIgnoreCase(String marca);

    List<GpuModel> findByMemoriaGreaterThanEqual(Integer memoria);

    List<GpuModel> findByConsumoLessThanEqual(Integer consumo);
}
