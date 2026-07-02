package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ItemOrcamentoFiltroRequest(
        @Positive
        Integer orcamentoId,
        @Positive
        Integer componenteId,
        @Positive
        Integer quantidadeMinima,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo
) {
}
