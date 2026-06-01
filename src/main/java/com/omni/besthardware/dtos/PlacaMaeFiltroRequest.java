package com.omni.besthardware.dtos;

import java.math.BigDecimal;

public record PlacaMaeFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        String marca,
        String socket,
        String chipset,
        String formato
) {
}
