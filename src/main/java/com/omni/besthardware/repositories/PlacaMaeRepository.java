package com.omni.besthardware.repositories;

import com.omni.besthardware.models.PlacaMaeModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlacaMaeRepository extends JpaRepository<PlacaMaeModel, Integer>, JpaSpecificationExecutor<PlacaMaeModel> {
    List<PlacaMaeModel> findByTipoIgnoreCase(String tipo);

    List<PlacaMaeModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<PlacaMaeModel> findByMarcaContainingIgnoreCase(String marca);

    List<PlacaMaeModel> findBySocketIgnoreCase(String socket);

    List<PlacaMaeModel> findByChipsetIgnoreCase(String chipset);

    List<PlacaMaeModel> findByFormatoIgnoreCase(String formato);
}
