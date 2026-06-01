package com.omni.besthardware.repositories;

import com.omni.besthardware.models.CpuModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CpuRepository extends JpaRepository<CpuModel, Integer>, JpaSpecificationExecutor<CpuModel> {
    List<CpuModel> findByTipoIgnoreCase(String tipo);

    List<CpuModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<CpuModel> findByModeloContainingIgnoreCase(String modelo);

    List<CpuModel> findBySocketIgnoreCase(String socket);

    List<CpuModel> findByFrequenciaGreaterThanEqual(Integer frequencia);

    List<CpuModel> findByConsumoLessThanEqual(Integer consumo);

    List<CpuModel> findByNucleosGreaterThanEqual(Integer nucleos);

    List<CpuModel> findByAnoLancamentoBetween(LocalDate dataInicial, LocalDate dataFinal);
}
