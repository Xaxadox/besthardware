package com.omni.besthardware.rest.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record CpuFiltroRequest(
        String tipo,
        @Positive
        BigDecimal precoMinimo,
        @Positive
        BigDecimal precoMaximo,
        String modelo,
        String socket,
        @Positive
        Integer frequenciaMinima,
        @Positive
        Integer consumoMaximo,
        @Positive
        Integer nucleosMinimos,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataInicial,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataFinal
) {
}
