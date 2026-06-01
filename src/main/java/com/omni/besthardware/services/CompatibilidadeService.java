package com.omni.besthardware.services;

import com.omni.besthardware.dtos.CompatibilidadeResponse;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.ArmazenamentoModel;
import com.omni.besthardware.models.ComponenteModel;
import com.omni.besthardware.models.CpuModel;
import com.omni.besthardware.models.FonteModel;
import com.omni.besthardware.models.GpuModel;
import com.omni.besthardware.models.PlacaMaeModel;
import com.omni.besthardware.models.RamModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CompatibilidadeService {

    private final ComponenteService componenteService;

    public CompatibilidadeService(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }

    public Optional<CompatibilidadeResponse> verificarPorIds(List<Integer> componenteIds) {
        List<ComponenteModel> componentes = new ArrayList<>();

        for (Integer componenteId : componenteIds) {
            Optional<ComponenteModel> componente = componenteService.buscarPorId(componenteId);
            if (componente.isEmpty()) {
                return Optional.empty();
            }

            componentes.add(componente.get());
        }

        return Optional.of(verificar(componentes));
    }

    public CompatibilidadeResponse verificar(List<ComponenteModel> componentes) {
        List<String> erros = new ArrayList<>();
        List<String> avisos = new ArrayList<>();

        List<CpuModel> cpus = filtrar(componentes, CpuModel.class);
        List<PlacaMaeModel> placasMae = filtrar(componentes, PlacaMaeModel.class);
        List<RamModel> memorias = filtrar(componentes, RamModel.class);
        List<GpuModel> gpus = filtrar(componentes, GpuModel.class);
        List<FonteModel> fontes = filtrar(componentes, FonteModel.class);
        List<ArmazenamentoModel> armazenamentos = filtrar(componentes, ArmazenamentoModel.class);

        validarQuantidade("CPU", cpus.size(), erros, avisos);
        validarQuantidade("placa-mae", placasMae.size(), erros, avisos);
        validarQuantidade("fonte", fontes.size(), erros, avisos);

        if (memorias.isEmpty()) {
            avisos.add("Nenhuma memoria RAM foi informada.");
        }

        if (armazenamentos.isEmpty()) {
            avisos.add("Nenhum armazenamento foi informado.");
        }

        CpuModel cpu = primeiro(cpus);
        PlacaMaeModel placaMae = primeiro(placasMae);
        FonteModel fonte = primeiro(fontes);
        GpuModel gpu = primeiro(gpus);

        if (cpu != null && placaMae != null && !cpu.getSocket().equalsIgnoreCase(placaMae.getSocket())) {
            erros.add("CPU socket " + cpu.getSocket() + " nao e compativel com placa-mae socket " + placaMae.getSocket() + ".");
        }

        if (placaMae != null) {
            String geracaoSuportada = inferirGeracaoRam(placaMae);
            if (geracaoSuportada == null) {
                avisos.add("Nao foi possivel inferir a geracao de RAM suportada pelo chipset " + placaMae.getChipset() + ".");
            } else {
                for (RamModel ram : memorias) {
                    if (!geracaoSuportada.equalsIgnoreCase(ram.getGeracao())) {
                        erros.add("RAM " + ram.getGeracao() + " nao e compativel com chipset " + placaMae.getChipset() + ", esperado " + geracaoSuportada + ".");
                    }
                }
            }
        }

        if (fonte != null && cpu != null) {
            int consumoEstimado = cpu.getConsumo() + 100;
            if (gpu != null) {
                consumoEstimado = cpu.getConsumo() + gpu.getConsumo() + 150;
            }

            if (fonte.getPotencia() < consumoEstimado) {
                erros.add("Fonte de " + fonte.getPotencia() + "W abaixo do consumo estimado de " + consumoEstimado + "W.");
            }
        }

        for (ArmazenamentoModel armazenamento : armazenamentos) {
            if ("NVME".equalsIgnoreCase(armazenamento.getPadrao()) && placaMae == null) {
                avisos.add("SSD NVME informado sem placa-mae para validar slot M.2.");
            }
        }

        if (gpu == null && cpu != null && !temGpuIntegrada(cpu)) {
            avisos.add("Nao ha GPU dedicada e a CPU nao parece ter video integrado pelo nome do modelo.");
        }

        return new CompatibilidadeResponse(
                erros.isEmpty(),
                erros,
                avisos,
                componentes.stream().map(DtoMapper::toComponenteResponse).toList()
        );
    }

    public String inferirGeracaoRam(PlacaMaeModel placaMae) {
        String chipset = placaMae.getChipset().toUpperCase();

        if (chipset.contains("B650") || chipset.contains("X670") || chipset.contains("A620")
                || chipset.contains("Z790") || chipset.contains("B760") || chipset.contains("H770")) {
            return "DDR5";
        }

        if (chipset.contains("B550") || chipset.contains("A520") || chipset.contains("X570")
                || chipset.contains("B450") || chipset.contains("A320") || chipset.contains("H510")
                || chipset.contains("H610") || chipset.contains("B660") || chipset.contains("Z690")) {
            return "DDR4";
        }

        return null;
    }

    public boolean temGpuIntegrada(CpuModel cpu) {
        String modelo = cpu.getModelo().toUpperCase();
        return modelo.contains("G") || modelo.contains("APU") || modelo.contains("INTEGRADA");
    }

    private void validarQuantidade(String tipo, int quantidade, List<String> erros, List<String> avisos) {
        if (quantidade == 0) {
            avisos.add("Nenhum componente do tipo " + tipo + " foi informado.");
        }

        if (quantidade > 1) {
            erros.add("Mais de um componente do tipo " + tipo + " foi informado.");
        }
    }

    private <T> List<T> filtrar(List<ComponenteModel> componentes, Class<T> tipo) {
        return componentes.stream()
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .toList();
    }

    private <T> T primeiro(List<T> itens) {
        return itens.isEmpty() ? null : itens.get(0);
    }
}
