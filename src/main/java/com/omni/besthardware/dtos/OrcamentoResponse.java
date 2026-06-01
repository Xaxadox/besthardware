package com.omni.besthardware.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrcamentoResponse(
        Integer id,
        String nome,
        LocalDateTime dataCriacao,
        BigDecimal precoTotal,
        Integer usuarioId,
        Integer perfilId,
        List<ItemOrcamentoResponse> itens
) {
}
