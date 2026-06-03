package com.omni.besthardware.rest.dto.request;

import java.math.BigDecimal;

public record ArmazenamentoFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        String tecnologia,
        String padrao,
        Integer memoriaMinima,
        Integer velocidadeLeituraMinima,
        Integer velocidadeEscritaMinima
) {
}
