package com.omni.besthardware.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OrcamentoRequest(
        @NotBlank
        @Size(max = 64)
        String nome,

        @NotNull
        @Positive
        Integer usuarioId,

        @NotNull
        @Positive
        Integer perfilId,

        @NotEmpty
        List<@Valid ItemOrcamentoRequest> itens
) {
}
