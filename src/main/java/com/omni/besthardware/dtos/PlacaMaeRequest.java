package com.omni.besthardware.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record PlacaMaeRequest(
        @NotBlank
        @Size(max = 64)
        String tipo,

        @NotNull
        @Positive
        BigDecimal preco,

        @NotBlank
        @Size(max = 128)
        String marca,

        @NotBlank
        @Size(max = 64)
        String socket,

        @NotBlank
        @Size(max = 64)
        String chipset,

        @NotBlank
        @Size(max = 32)
        String formato
) {
}
