package com.omni.besthardware.repositories;

import com.omni.besthardware.models.MonitorModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorRepository extends JpaRepository<MonitorModel, Integer> {
    List<MonitorModel> findByTipoIgnoreCase(String tipo);

    List<MonitorModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<MonitorModel> findByMarcaContainingIgnoreCase(String marca);

    List<MonitorModel> findByTamanho(String tamanho);

    List<MonitorModel> findByResolucaoIgnoreCase(String resolucao);

    List<MonitorModel> findByFrequenciaGreaterThanEqual(Integer frequencia);

    List<MonitorModel> findByTecnologiaIgnoreCase(String tecnologia);
}
