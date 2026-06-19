package com.omni.besthardware.mappers;

import com.omni.besthardware.rest.dto.response.ArmazenamentoResponse;
import com.omni.besthardware.rest.dto.response.ComponenteGenericoResponse;
import com.omni.besthardware.rest.dto.response.ComponenteResponse;
import com.omni.besthardware.rest.dto.response.CpuResponse;
import com.omni.besthardware.rest.dto.response.FonteResponse;
import com.omni.besthardware.rest.dto.response.GpuResponse;
import com.omni.besthardware.rest.dto.response.MonitorResponse;
import com.omni.besthardware.rest.dto.response.PlacaMaeResponse;
import com.omni.besthardware.rest.dto.response.RamResponse;
import com.omni.besthardware.model.ArmazenamentoModel;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.model.CpuModel;
import com.omni.besthardware.model.FonteModel;
import com.omni.besthardware.model.GpuModel;
import com.omni.besthardware.model.MonitorModel;
import com.omni.besthardware.model.PlacaMaeModel;
import com.omni.besthardware.model.RamModel;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ComponenteMapper {

    public CpuResponse toCpuResponse(CpuModel cpu) {
        return toCpuResponse(cpu, cpu.getPreco());
    }

    public CpuResponse toCpuResponse(CpuModel cpu, BigDecimal preco) {
        return new CpuResponse(
                cpu.getId(), "CPU", cpu.getTipo(), preco,
                cpu.getModelo(), cpu.getSocket(), cpu.getFrequencia(), cpu.getConsumo(),
                cpu.getAnoLancamento(), cpu.getNucleos()
        );
    }

    public GpuResponse toGpuResponse(GpuModel gpu) {
        return toGpuResponse(gpu, gpu.getPreco());
    }

    public GpuResponse toGpuResponse(GpuModel gpu, BigDecimal preco) {
        return new GpuResponse(
                gpu.getId(), "GPU", gpu.getTipo(), preco,
                gpu.getModelo(), gpu.getMarca(), gpu.getMemoria(), gpu.getConsumo()
        );
    }

    public RamResponse toRamResponse(RamModel ram) {
        return toRamResponse(ram, ram.getPreco());
    }

    public RamResponse toRamResponse(RamModel ram, BigDecimal preco) {
        return new RamResponse(
                ram.getId(), "RAM", ram.getTipo(), preco,
                ram.getMarca(), ram.getGeracao(), ram.getFrequencia(), ram.getMemoria()
        );
    }

    public FonteResponse toFonteResponse(FonteModel fonte) {
        return toFonteResponse(fonte, fonte.getPreco());
    }

    public FonteResponse toFonteResponse(FonteModel fonte, BigDecimal preco) {
        return new FonteResponse(
                fonte.getId(), "Fonte", fonte.getTipo(), preco,
                fonte.getMarca(), fonte.getPotencia(), fonte.getCertificacao()
        );
    }

    public ArmazenamentoResponse toArmazenamentoResponse(ArmazenamentoModel armazenamento) {
        return toArmazenamentoResponse(armazenamento, armazenamento.getPreco());
    }

    public ArmazenamentoResponse toArmazenamentoResponse(ArmazenamentoModel armazenamento, BigDecimal preco) {
        return new ArmazenamentoResponse(
                armazenamento.getId(), "Armazenamento", armazenamento.getTipo(), preco,
                armazenamento.getTecnologia(), armazenamento.getPadrao(), armazenamento.getMemoria(),
                armazenamento.getVelocidadeLeitura(), armazenamento.getVelocidadeEscrita()
        );
    }

    public MonitorResponse toMonitorResponse(MonitorModel monitor) {
        return toMonitorResponse(monitor, monitor.getPreco());
    }

    public MonitorResponse toMonitorResponse(MonitorModel monitor, BigDecimal preco) {
        return new MonitorResponse(
                monitor.getId(), "Monitor", monitor.getTipo(), preco,
                monitor.getMarca(), monitor.getTamanho(), monitor.getResolucao(),
                monitor.getFrequencia(), monitor.getTecnologia()
        );
    }

    public PlacaMaeResponse toPlacaMaeResponse(PlacaMaeModel placaMae) {
        return toPlacaMaeResponse(placaMae, placaMae.getPreco());
    }

    public PlacaMaeResponse toPlacaMaeResponse(PlacaMaeModel placaMae, BigDecimal preco) {
        return new PlacaMaeResponse(
                placaMae.getId(), "PlacaMae", placaMae.getTipo(), preco,
                placaMae.getMarca(), placaMae.getSocket(), placaMae.getChipset(), placaMae.getFormato()
        );
    }

    public ComponenteResponse toComponenteResponse(ComponenteModel componente) {
        return toComponenteResponse(componente, componente.getPreco());
    }

    public ComponenteResponse toComponenteResponse(ComponenteModel componente, BigDecimal preco) {
        return switch (componente) {
            case CpuModel cpu -> toCpuResponse(cpu, preco);
            case GpuModel gpu -> toGpuResponse(gpu, preco);
            case RamModel ram -> toRamResponse(ram, preco);
            case FonteModel fonte -> toFonteResponse(fonte, preco);
            case ArmazenamentoModel armazenamento -> toArmazenamentoResponse(armazenamento, preco);
            case MonitorModel monitor -> toMonitorResponse(monitor, preco);
            case PlacaMaeModel placaMae -> toPlacaMaeResponse(placaMae, preco);
            default -> new ComponenteGenericoResponse(componente.getId(), "Componente", componente.getTipo(), preco);
        };
    }
}