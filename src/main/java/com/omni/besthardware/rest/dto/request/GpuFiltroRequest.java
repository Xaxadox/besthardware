package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record GpuFiltroRequest(
        String tipo,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo,
        String modelo,
        String marca,
        @Positive
        Integer memoriaMinima,
        @Positive
        Integer consumoMaximo
) {
}
