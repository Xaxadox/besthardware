package com.omni.besthardware.rest.dto.request;

import java.math.BigDecimal;

public record ItemOrcamentoFiltroRequest(
        Integer orcamentoId,
        Integer componenteId,
        Integer quantidadeMinima,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo
) {
}
