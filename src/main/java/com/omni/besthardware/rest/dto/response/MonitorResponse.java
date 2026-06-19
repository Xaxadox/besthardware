package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;

public record MonitorResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco,
        String marca,
        String tamanho,
        String resolucao,
        Integer frequencia,
        String tecnologia
) implements ComponenteResponse {
}
