package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record RecomendacaoResponse(
        PerfilUsoResponse perfil,
        BigDecimal precoTotal,
        List<ComponenteResponse> componentes,
        CompatibilidadeResponse compatibilidade,
        List<String> observacoes
) {
}
