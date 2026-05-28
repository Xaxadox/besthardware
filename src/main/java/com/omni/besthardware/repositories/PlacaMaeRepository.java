package com.omni.besthardware.repositories;

import com.omni.besthardware.models.PlacaMaeModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacaMaeRepository extends JpaRepository<PlacaMaeModel, Integer> {
    List<PlacaMaeModel> findByTipoIgnoreCase(String tipo);

    List<PlacaMaeModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<PlacaMaeModel> findByMarcaContainingIgnoreCase(String marca);

    List<PlacaMaeModel> findBySocketIgnoreCase(String socket);

    List<PlacaMaeModel> findByChipsetIgnoreCase(String chipset);

    List<PlacaMaeModel> findByFormatoIgnoreCase(String formato);
}
