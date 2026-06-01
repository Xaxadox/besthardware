package com.omni.besthardware.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CompatibilidadeRequest(
        @NotEmpty
        List<@NotNull @Positive Integer> componenteIds
) {
}
