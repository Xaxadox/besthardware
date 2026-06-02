package com.omni.besthardware.dtos;

import java.math.BigDecimal;

public record ComponenteFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        Integer perfilId,
        Integer componenteCompativelId
) {
}
