package com.omni.besthardware.rest.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record CpuFiltroRequest(
        String tipo,
        BigDecimal precoMinimo,
        BigDecimal precoMaximo,
        String modelo,
        String socket,
        Integer frequenciaMinima,
        Integer consumoMaximo,
        Integer nucleosMinimos,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataInicial,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate dataFinal
) {
}
