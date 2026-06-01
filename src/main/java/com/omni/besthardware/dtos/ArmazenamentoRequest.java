package com.omni.besthardware.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ArmazenamentoRequest(
        @NotBlank
        @Size(max = 64)
        String tipo,

        @NotNull
        @Positive
        BigDecimal preco,

        @NotBlank
        @Size(max = 64)
        String tecnologia,

        @NotBlank
        @Size(max = 64)
        String padrao,

        @NotNull
        @Positive
        Integer velocidadeEscrita,

        @NotNull
        @Positive
        Integer velocidadeLeitura,

        @NotNull
        @Positive
        Integer memoria
) {
}
