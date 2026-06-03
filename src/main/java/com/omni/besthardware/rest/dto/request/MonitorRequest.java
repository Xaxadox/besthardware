package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record MonitorRequest(
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
        @Size(max = 2)
        String tamanho,

        @NotBlank
        @Size(max = 64)
        String resolucao,

        @NotNull
        @Positive
        Integer frequencia,

        @NotBlank
        @Size(max = 64)
        String tecnologia
) {
}
