package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PerfilRequest(
        @NotBlank
        @Size(max = 128)
        String nome,

        List<Integer> componenteIds
) {
}
