package com.omni.besthardware.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record OfertaPrecoRequest(
        @NotBlank
        @Size(max = 128)
        String loja,

        @NotNull
        @Positive
        BigDecimal precoAvista,

        @Positive
        BigDecimal precoParcelado,

        @Positive
        Integer parcelas,

        @Size(max = 64)
        String cupom,

        @NotBlank
        @Size(max = 512)
        String urlProduto,

        @NotBlank
        @Size(max = 255)
        String fonte,

        @Size(max = 255)
        String observacoes,

        @NotNull
        @PastOrPresent
        LocalDate dataColeta,

        @NotNull
        @Positive
        Integer componenteId
) {
}
