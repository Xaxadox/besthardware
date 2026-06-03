package com.omni.besthardware.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OfertaPrecoResponse(
        Integer id,
        String loja,
        BigDecimal precoAvista,
        BigDecimal precoParcelado,
        Integer parcelas,
        String cupom,
        String urlProduto,
        String fonte,
        String observacoes,
        LocalDate dataColeta,
        Integer componenteId,
        String componenteTipo
) {
}
