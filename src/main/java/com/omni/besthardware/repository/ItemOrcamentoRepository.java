package com.omni.besthardware.repository;

import com.omni.besthardware.model.ItemOrcamentoModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio responsavel pelo acesso aos dados de ItemOrcamento.
 */
public interface ItemOrcamentoRepository extends JpaRepository<ItemOrcamentoModel, Integer>, JpaSpecificationExecutor<ItemOrcamentoModel> {
    List<ItemOrcamentoModel> findByOrcamentoId(Integer orcamentoId);

    List<ItemOrcamentoModel> findByComponenteId(Integer componenteId);

    List<ItemOrcamentoModel> findByOrcamentoIdAndComponenteId(Integer orcamentoId, Integer componenteId);

    List<ItemOrcamentoModel> findByQuantidadeGreaterThanEqual(Integer quantidade);

    List<ItemOrcamentoModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);
}
