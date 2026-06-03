package com.omni.besthardware.rest.dto.request;

import java.math.BigDecimal;

public record GpuFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        String modelo,
        String marca,
        Integer memoriaMinima,
        Integer consumoMaximo
) {
}
