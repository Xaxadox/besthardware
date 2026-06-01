package com.omni.besthardware.repositories;

import com.omni.besthardware.models.RamModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RamRepository extends JpaRepository<RamModel, Integer>, JpaSpecificationExecutor<RamModel> {
    List<RamModel> findByTipoIgnoreCase(String tipo);

    List<RamModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<RamModel> findByGeracaoIgnoreCase(String geracao);

    List<RamModel> findByFrequenciaGreaterThanEqual(Integer frequencia);

    List<RamModel> findByMarcaContainingIgnoreCase(String marca);

    List<RamModel> findByMemoriaGreaterThanEqual(Integer memoria);
}
