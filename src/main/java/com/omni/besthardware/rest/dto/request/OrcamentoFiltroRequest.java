package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record OrcamentoFiltroRequest(
        String nome,
        @Positive
        Integer usuarioId,
        @Positive
        Integer perfilId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dataInicial,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dataFinal,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo
) {
}
