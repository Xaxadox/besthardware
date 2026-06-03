package com.omni.besthardware.rest.dto.request;

import java.math.BigDecimal;

public record MonitorFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        String marca,
        String tamanho,
        String resolucao,
        Integer frequenciaMinima,
        String tecnologia
) {
}
