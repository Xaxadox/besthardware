package com.omni.besthardware.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record OrcamentoFiltroRequest(
        String nome,
        Integer usuarioId,
        Integer perfilId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dataInicial,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime dataFinal,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo
) {
}
