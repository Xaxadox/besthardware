package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CpuResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco,
        String modelo,
        String socket,
        Integer frequencia,
        Integer consumo,
        LocalDate anoLancamento,
        Integer nucleos
) implements ComponenteResponse {
}
