package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ArmazenamentoFiltroRequest(
        String tipo,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo,
        String tecnologia,
        String padrao,
        @Positive
        Integer memoriaMinima,
        @Positive
        Integer velocidadeLeituraMinima,
        @Positive
        Integer velocidadeEscritaMinima
) {
}
