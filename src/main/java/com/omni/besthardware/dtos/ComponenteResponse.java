package com.omni.besthardware.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ComponenteResponse(
        Integer id,
        String categoria,
        String tipo,
        BigDecimal preco,
        String modelo,
        String marca,
        String socket,
        Integer frequencia,
        Integer consumo,
        LocalDate anoLancamento,
        Integer nucleos,
        Integer memoria,
        String geracao,
        String certificacao,
        Integer potencia,
        String tecnologia,
        String padrao,
        Integer velocidadeEscrita,
        Integer velocidadeLeitura,
        String tamanho,
        String resolucao,
        String chipset,
        String formato
) {
}
