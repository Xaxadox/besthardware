package com.omni.besthardware.dtos;

import java.math.BigDecimal;

public record RamFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        String geracao,
        Integer frequenciaMinima,
        String marca,
        Integer memoriaMinima
) {
}
