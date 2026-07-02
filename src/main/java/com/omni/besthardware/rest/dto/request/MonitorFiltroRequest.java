package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record MonitorFiltroRequest(
        String tipo,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo,
        String marca,
        String tamanho,
        String resolucao,
        @Positive
        Integer frequenciaMinima,
        String tecnologia
) {
}
