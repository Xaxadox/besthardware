package com.omni.besthardware.rest.dto.request;

import java.math.BigDecimal;

public record ComponenteFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        Integer perfilId,
        Integer componenteCompativelId
) {
}
