package com.omni.besthardware.dtos;

import java.math.BigDecimal;

public record FonteFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        String marca,
        Integer potenciaMinima,
        String certificacao
) {
}
