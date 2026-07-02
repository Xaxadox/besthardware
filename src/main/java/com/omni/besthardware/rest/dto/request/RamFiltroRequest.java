package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RamFiltroRequest(
        String tipo,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo,
        String geracao,
        @Positive
        Integer frequenciaMinima,
        String marca,
        @Positive
        Integer memoriaMinima
) {
}
