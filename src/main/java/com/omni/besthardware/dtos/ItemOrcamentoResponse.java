package com.omni.besthardware.dtos;

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
