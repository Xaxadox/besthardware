package com.omni.besthardware.repository;

import com.omni.besthardware.model.OrcamentoModel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio responsavel pelo acesso aos dados de Orcamento.
 */
public interface OrcamentoRepository extends JpaRepository<OrcamentoModel, Integer>, JpaSpecificationExecutor<OrcamentoModel> {
    List<OrcamentoModel> findByNomeContainingIgnoreCase(String nome);

    List<OrcamentoModel> findByUsuarioId(Integer usuarioId);

    List<OrcamentoModel> findByPerfilId(Integer perfilId);

    List<OrcamentoModel> findByDataCriacaoBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);

    List<OrcamentoModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);
}
