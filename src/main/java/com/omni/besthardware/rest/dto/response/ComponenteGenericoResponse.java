package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;


public record ComponenteGenericoResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco
) implements ComponenteResponse {
}
