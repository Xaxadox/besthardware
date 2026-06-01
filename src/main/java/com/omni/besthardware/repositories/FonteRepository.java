package com.omni.besthardware.repositories;

import com.omni.besthardware.models.FonteModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FonteRepository extends JpaRepository<FonteModel, Integer>, JpaSpecificationExecutor<FonteModel> {
    List<FonteModel> findByTipoIgnoreCase(String tipo);

    List<FonteModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<FonteModel> findByMarcaContainingIgnoreCase(String marca);

    List<FonteModel> findByPotenciaGreaterThanEqual(Integer potencia);

    List<FonteModel> findByCertificacaoContainingIgnoreCase(String certificacao);
}
