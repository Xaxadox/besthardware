package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;

public record RamResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco,
        String marca,
        String geracao,
        Integer frequencia,
        Integer memoria
) implements ComponenteResponse {
}
