package com.omni.besthardware.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record RamRequest(
        @NotBlank
        @Size(max = 64)
        String tipo,

        @NotNull
        @Positive
        BigDecimal preco,

        @NotBlank
        @Size(max = 64)
        String geracao,

        @NotNull
        @Positive
        Integer frequencia,

        @NotBlank
        @Size(max = 128)
        String marca,

        @NotNull
        @Positive
        Integer memoria
) {
}
