package com.omni.besthardware.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CpuRequest(
        @NotBlank
        @Size(max = 64)
        String tipo,

        @NotNull
        @Positive
        BigDecimal preco,

        @NotBlank
        @Size(max = 128)
        String modelo,

        @NotNull
        @Positive
        Integer frequencia,

        @NotNull
        @Positive
        Integer consumo,

        @NotNull
        LocalDate anoLancamento,

        @NotNull
        @Positive
        Integer nucleos,

        @NotBlank
        @Size(max = 64)
        String socket
) {
}
