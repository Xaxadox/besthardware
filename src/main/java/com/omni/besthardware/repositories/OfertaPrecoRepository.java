package com.omni.besthardware.repositories;

import com.omni.besthardware.models.OfertaPrecoModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfertaPrecoRepository extends JpaRepository<OfertaPrecoModel, Integer>, JpaSpecificationExecutor<OfertaPrecoModel> {
    List<OfertaPrecoModel> findByComponenteId(Integer componenteId);

    List<OfertaPrecoModel> findByLojaContainingIgnoreCase(String loja);

    List<OfertaPrecoModel> findByFonteContainingIgnoreCase(String fonte);

    List<OfertaPrecoModel> findByPrecoAvistaBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<OfertaPrecoModel> findByDataColetaBetween(LocalDate dataInicial, LocalDate dataFinal);

    Optional<OfertaPrecoModel> findFirstByComponenteIdOrderByPrecoAvistaAsc(Integer componenteId);
}
