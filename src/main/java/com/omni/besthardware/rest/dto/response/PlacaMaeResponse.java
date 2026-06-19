package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;

public record PlacaMaeResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco,
        String marca,
        String socket,
        String chipset,
        String formato
) implements ComponenteResponse {
}
