package com.omni.besthardware.dtos;

import java.util.List;

public record PerfilResponse(
        Integer id,
        String nome,
        List<ComponenteResponse> componentes
) {
}
