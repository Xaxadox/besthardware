package com.omni.besthardware.rest.dto.response;

import java.util.List;

public record AuthResponse(
        String token,
        String tipo,
        long expiraEmSegundos,
        String usuario,
        List<String> permissoes
) {
}
