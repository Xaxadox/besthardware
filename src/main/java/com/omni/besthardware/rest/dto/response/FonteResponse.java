package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;

public record FonteResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco,
        String marca,
        Integer potencia,
        String certificacao
) implements ComponenteResponse {
}
