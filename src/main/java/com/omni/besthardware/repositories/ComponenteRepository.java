package com.omni.besthardware.repositories;

import com.omni.besthardware.models.ComponenteModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponenteRepository extends JpaRepository<ComponenteModel, Integer> {
    List<ComponenteModel> findByTipoIgnoreCase(String tipo);

    List<ComponenteModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<ComponenteModel> findByPerfisId(Integer perfilId);

    List<ComponenteModel> findByComponentesCompativeisId(Integer componenteCompativelId);
}
