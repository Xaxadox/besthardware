package com.omni.besthardware.rest.dto.response;

import java.util.List;

public record CompatibilidadeResponse(
        boolean compativel,
        List<String> erros,
        List<String> avisos,
        List<ComponenteResponse> componentes
) {
}
