package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ComponenteFiltroRequest(
        String tipo,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo,
        @Positive
        Integer perfilId,
        @Positive
        Integer componenteCompativelId
) {
}
