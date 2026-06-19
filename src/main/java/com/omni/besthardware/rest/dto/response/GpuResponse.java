package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;

public record GpuResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco,
        String modelo,
        String marca,
        Integer memoria,
        Integer consumo
) implements ComponenteResponse {
}
