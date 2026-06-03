package com.omni.besthardware.repository;

import com.omni.besthardware.model.MonitorModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio responsavel pelo acesso aos dados de Monitor.
 */
public interface MonitorRepository extends JpaRepository<MonitorModel, Integer>, JpaSpecificationExecutor<MonitorModel> {
    List<MonitorModel> findByTipoIgnoreCase(String tipo);

    List<MonitorModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<MonitorModel> findByMarcaContainingIgnoreCase(String marca);

    List<MonitorModel> findByTamanho(String tamanho);

    List<MonitorModel> findByResolucaoIgnoreCase(String resolucao);

    List<MonitorModel> findByFrequenciaGreaterThanEqual(Integer frequencia);

    List<MonitorModel> findByTecnologiaIgnoreCase(String tecnologia);
}
