package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;

public record ArmazenamentoResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco,
        String tecnologia,
        String padrao,
        Integer memoria,
        Integer velocidadeLeitura,
        Integer velocidadeEscrita
) implements ComponenteResponse {
}
