package com.omni.besthardware.rest.dto.response;

import java.util.List;

public record PerfilResponse(
        Integer id,
        String nome,
        List<ComponenteResponse> componentes
) {
}
