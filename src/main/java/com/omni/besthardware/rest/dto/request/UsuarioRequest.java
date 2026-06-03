package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank
        @Size(max = 255)
        String nome,

        @NotBlank
        @Size(max = 128)
        String email
) {
}
