package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record OfertaPrecoFiltroRequest(
        @Positive
        Integer componenteId,
        String loja,
        @Positive
        BigDecimal precoAvistaMinimo,
        @Positive
        BigDecimal precoAvistaMaximo,
        @Positive
        BigDecimal precoParceladoMinimo,
        @Positive
        BigDecimal precoParceladoMaximo,
        @Positive
        Integer parcelasMaximas,
        String cupom,
        String fonte,
        LocalDate dataInicial,
        LocalDate dataFinal
) {
}
