package com.omni.besthardware.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemOrcamentoRequest(
        @NotNull
        @Positive
        Integer componenteId,

        @NotNull
        @Positive
        Integer quantidade
) {
}
