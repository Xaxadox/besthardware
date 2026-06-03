package com.omni.besthardware.rest.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OfertaPrecoFiltroRequest(
        Integer componenteId,
        String loja,
        BigDecimal precoAvistaMinimo,
        BigDecimal precoAvistaMaximo,
        BigDecimal precoParceladoMinimo,
        BigDecimal precoParceladoMaximo,
        Integer parcelasMaximas,
        String cupom,
        String fonte,
        LocalDate dataInicial,
        LocalDate dataFinal
) {
}
