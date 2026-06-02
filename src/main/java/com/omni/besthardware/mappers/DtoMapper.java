package com.omni.besthardware.mappers;

import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.dtos.PerfilResponse;
import com.omni.besthardware.dtos.UsuarioResponse;
import com.omni.besthardware.models.ArmazenamentoModel;
import com.omni.besthardware.models.ComponenteModel;
import com.omni.besthardware.models.CpuModel;
import com.omni.besthardware.models.FonteModel;
import com.omni.besthardware.models.GpuModel;
import com.omni.besthardware.models.MonitorModel;
import com.omni.besthardware.models.PerfilModel;
import com.omni.besthardware.models.PlacaMaeModel;
import com.omni.besthardware.models.RamModel;
import com.omni.besthardware.models.UsuarioModel;
import java.math.BigDecimal;
import java.util.List;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static UsuarioResponse toUsuarioResponse(UsuarioModel usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public static PerfilResponse toPerfilResponse(PerfilModel perfil) {
        List<ComponenteResponse> componentes = perfil.getComponentes().stream()
                .map(DtoMapper::toComponenteResponse)
                .toList();

        return new PerfilResponse(perfil.getId(), perfil.getNome(), componentes);
    }

    public static ComponenteResponse toComponenteResponse(ComponenteModel componente) {
        return toComponenteResponse(componente, componente.getPreco());
    }

    public static ComponenteResponse toComponenteResponse(ComponenteModel componente, BigDecimal preco) {
        if (componente instanceof CpuModel cpu) {
            return new ComponenteResponse(
                    cpu.getId(),
                    "CPU",
                    cpu.getTipo(),
                    preco,
                    cpu.getModelo(),
                    null,
                    cpu.getSocket(),
                    cpu.getFrequencia(),
                    cpu.getConsumo(),
                    cpu.getAnoLancamento(),
                    cpu.getNucleos(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (componente instanceof GpuModel gpu) {
            return new ComponenteResponse(
                    gpu.getId(),
                    "GPU",
                    gpu.getTipo(),
                    preco,
                    gpu.getModelo(),
                    gpu.getMarca(),
                    null,
                    null,
                    gpu.getConsumo(),
                    null,
                    null,
                    gpu.getMemoria(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (componente instanceof RamModel ram) {
            return new ComponenteResponse(
                    ram.getId(),
                    "RAM",
                    ram.getTipo(),
                    preco,
                    null,
                    ram.getMarca(),
                    null,
                    ram.getFrequencia(),
                    null,
                    null,
                    null,
                    ram.getMemoria(),
                    ram.getGeracao(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (componente instanceof FonteModel fonte) {
            return new ComponenteResponse(
                    fonte.getId(),
                    "Fonte",
                    fonte.getTipo(),
                    preco,
                    null,
                    fonte.getMarca(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    fonte.getCertificacao(),
                    fonte.getPotencia(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (componente instanceof ArmazenamentoModel armazenamento) {
            return new ComponenteResponse(
                    armazenamento.getId(),
                    "Armazenamento",
                    armazenamento.getTipo(),
                    preco,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    armazenamento.getMemoria(),
                    null,
                    null,
                    null,
                    armazenamento.getTecnologia(),
                    armazenamento.getPadrao(),
                    armazenamento.getVelocidadeEscrita(),
                    armazenamento.getVelocidadeLeitura(),
                    null,
                    null,
                    null,
                    null
            );
        }

        if (componente instanceof MonitorModel monitor) {
            return new ComponenteResponse(
                    monitor.getId(),
                    "Monitor",
                    monitor.getTipo(),
                    preco,
                    null,
                    monitor.getMarca(),
                    null,
                    monitor.getFrequencia(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    monitor.getTecnologia(),
                    null,
                    null,
                    null,
                    monitor.getTamanho(),
                    monitor.getResolucao(),
                    null,
                    null
            );
        }

        if (componente instanceof PlacaMaeModel placaMae) {
            return new ComponenteResponse(
                    placaMae.getId(),
                    "PlacaMae",
                    placaMae.getTipo(),
                    preco,
                    null,
                    placaMae.getMarca(),
                    placaMae.getSocket(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    placaMae.getChipset(),
                    placaMae.getFormato()
            );
        }

        return new ComponenteResponse(
                componente.getId(),
                "Componente",
                componente.getTipo(),
                preco,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
