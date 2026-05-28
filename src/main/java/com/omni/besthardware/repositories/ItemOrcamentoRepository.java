package com.omni.besthardware.repositories;

import com.omni.besthardware.models.ItemOrcamentoModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamentoModel, Integer> {
    List<ItemOrcamentoModel> findByOrcamentoId(Integer orcamentoId);

    List<ItemOrcamentoModel> findByComponenteId(Integer componenteId);

    List<ItemOrcamentoModel> findByOrcamentoIdAndComponenteId(Integer orcamentoId, Integer componenteId);

    List<ItemOrcamentoModel> findByQuantidadeGreaterThanEqual(Integer quantidade);

    List<ItemOrcamentoModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);
}
