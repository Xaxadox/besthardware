package com.omni.besthardware.dtos;

import java.util.List;

public record PerfilUsoResponse(
        String codigo,
        String nome,
        String objetivo,
        boolean exigeGpuDedicada,
        String processador,
        String gpu,
        String memoriaRam,
        String armazenamento,
        String fonte,
        String monitor,
        List<String> usosIndicados
) {
}
