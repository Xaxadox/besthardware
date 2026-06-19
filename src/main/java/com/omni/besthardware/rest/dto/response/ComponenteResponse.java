package com.omni.besthardware.rest.dto.response;

import java.math.BigDecimal;


public sealed interface ComponenteResponse
        permits CpuResponse, GpuResponse, RamResponse, FonteResponse,
        ArmazenamentoResponse, MonitorResponse, PlacaMaeResponse, ComponenteGenericoResponse {

    Integer id();

    String categoria();

    String tipo();

    BigDecimal preco();
}
