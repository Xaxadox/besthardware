package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PlacaMaeFiltroRequest(
        String tipo,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo,
        String marca,
        String socket,
        String chipset,
        String formato
) {
}
