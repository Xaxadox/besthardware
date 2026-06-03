package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;

public record ItemOrcamentoResponse(
        Integer id,
        Integer orcamentoId,
        Integer componenteId,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
}
