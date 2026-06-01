package com.omni.besthardware.repositories;

import com.omni.besthardware.models.ArmazenamentoModel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArmazenamentoRepository extends JpaRepository<ArmazenamentoModel, Integer>, JpaSpecificationExecutor<ArmazenamentoModel> {
    List<ArmazenamentoModel> findByTipoIgnoreCase(String tipo);

    List<ArmazenamentoModel> findByPrecoBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    List<ArmazenamentoModel> findByTecnologiaIgnoreCase(String tecnologia);

    List<ArmazenamentoModel> findByPadraoIgnoreCase(String padrao);

    List<ArmazenamentoModel> findByMemoriaGreaterThanEqual(Integer memoria);

    List<ArmazenamentoModel> findByVelocidadeLeituraGreaterThanEqual(Integer velocidadeLeitura);

    List<ArmazenamentoModel> findByVelocidadeEscritaGreaterThanEqual(Integer velocidadeEscrita);
}
