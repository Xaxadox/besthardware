package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OrcamentoAtualizacaoRequest(
        @NotBlank
        @Size(max = 64)
        String nome,

        @NotNull
        @Positive
        Integer usuarioId,

        @NotNull
        @Positive
        Integer perfilId
) {
}
