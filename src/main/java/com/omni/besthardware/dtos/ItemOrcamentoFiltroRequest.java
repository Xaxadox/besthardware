package com.omni.besthardware.dtos;

import java.math.BigDecimal;

public record ItemOrcamentoFiltroRequest(
        Integer orcamentoId,
        Integer componenteId,
        Integer quantidadeMinima,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo
) {
}
