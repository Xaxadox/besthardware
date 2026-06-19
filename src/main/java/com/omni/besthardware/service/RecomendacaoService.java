package com.omni.besthardware.service;

import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.ArmazenamentoModel;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.model.CpuModel;
import com.omni.besthardware.model.FonteModel;
import com.omni.besthardware.model.GpuModel;
import com.omni.besthardware.model.MonitorModel;
import com.omni.besthardware.model.PlacaMaeModel;
import com.omni.besthardware.model.RamModel;
import com.omni.besthardware.rest.dto.response.CompatibilidadeResponse;
import com.omni.besthardware.rest.dto.response.PerfilUsoResponse;
import com.omni.besthardware.rest.dto.response.RecomendacaoResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RecomendacaoService {

    private final PerfilUsoService perfilUsoService;
    private final CompatibilidadeService compatibilidadeService;
    private final CpuService cpuService;
    private final PlacaMaeService placaMaeService;
    private final GpuService gpuService;
    private final RamService ramService;
    private final FonteService fonteService;
    private final ArmazenamentoService armazenamentoService;
    private final MonitorService monitorService;
    private final OfertaPrecoService ofertaPrecoService;
    private final ComponenteMapper componenteMapper;

    public RecomendacaoService(
            PerfilUsoService perfilUsoService,
            CompatibilidadeService compatibilidadeService,
            CpuService cpuService,
            PlacaMaeService placaMaeService,
            GpuService gpuService,
            RamService ramService,
            FonteService fonteService,
            ArmazenamentoService armazenamentoService,
            MonitorService monitorService,
            OfertaPrecoService ofertaPrecoService,
            ComponenteMapper componenteMapper
    ) {
        this.perfilUsoService = perfilUsoService;
        this.compatibilidadeService = compatibilidadeService;
        this.cpuService = cpuService;
        this.placaMaeService = placaMaeService;
        this.gpuService = gpuService;
        this.ramService = ramService;
        this.fonteService = fonteService;
        this.armazenamentoService = armazenamentoService;
        this.monitorService = monitorService;
        this.ofertaPrecoService = ofertaPrecoService;
        this.componenteMapper = componenteMapper;
    }

    public List<RecomendacaoResponse> listarTodas() {
        return perfilUsoService.listarTodos().stream()
                .map(perfil -> recomendar(perfil.codigo()))
                .flatMap(Optional::stream)
                .toList();
    }

    public Optional<RecomendacaoResponse> recomendar(String codigoPerfil) {
        Optional<PerfilUsoResponse> perfil = perfilUsoService.buscarPorCodigo(codigoPerfil);
        if (perfil.isEmpty()) {
            return Optional.empty();
        }

        CriterioRecomendacao criterio = criterio(codigoPerfil);
        List<String> observacoes = new ArrayList<>();
        List<ComponenteModel> componentes = new ArrayList<>();

        Optional<CpuModel> cpu = selecionarCpu(criterio);
        cpu.ifPresentOrElse(componentes::add, () -> observacoes.add("Nenhuma CPU encontrada para o perfil."));

        Optional<PlacaMaeModel> placaMae = cpu.flatMap(this::selecionarPlacaMae);
        placaMae.ifPresentOrElse(componentes::add, () -> observacoes.add("Nenhuma placa-mae compativel foi encontrada."));

        Optional<RamModel> ram = selecionarRam(criterio, placaMae.orElse(null));
        ram.ifPresentOrElse(componentes::add, () -> observacoes.add("Nenhuma memoria RAM encontrada para o perfil."));

        Optional<GpuModel> gpu = Optional.empty();
        if (criterio.exigeGpuDedicada()) {
            gpu = selecionarGpu(criterio);
            gpu.ifPresentOrElse(componentes::add, () -> observacoes.add("Nenhuma GPU dedicada encontrada para o perfil."));
        } else if (cpu.isPresent() && !compatibilidadeService.temGpuIntegrada(cpu.get())) {
            observacoes.add("Perfil de trabalho recomenda CPU com video integrado; a melhor CPU encontrada pode exigir GPU dedicada.");
        }

        Optional<ArmazenamentoModel> armazenamento = selecionarArmazenamento(criterio);
        armazenamento.ifPresentOrElse(componentes::add, () -> observacoes.add("Nenhum armazenamento encontrado para o perfil."));

        Optional<FonteModel> fonte = selecionarFonte(criterio, cpu.orElse(null), gpu.orElse(null));
        fonte.ifPresentOrElse(componentes::add, () -> observacoes.add("Nenhuma fonte encontrada para o consumo estimado."));

        Optional<MonitorModel> monitor = selecionarMonitor(criterio);
        monitor.ifPresentOrElse(componentes::add, () -> observacoes.add("Nenhum monitor encontrado para o perfil."));

        CompatibilidadeResponse compatibilidade = compatibilidadeService.verificar(componentes);
        BigDecimal precoTotal = componentes.stream()
                .map(ofertaPrecoService::calcularPrecoPreferencial)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Optional.of(new RecomendacaoResponse(
                perfil.get(),
                precoTotal,
                componentes.stream()
                        .map(componente -> componenteMapper.toComponenteResponse(
                                componente,
                                ofertaPrecoService.calcularPrecoPreferencial(componente)
                        ))
                        .toList(),
                compatibilidade,
                observacoes
        ));
    }

    public RecomendacaoResponse recomendarObrigatorio(String codigoPerfil) {
        return recomendar(codigoPerfil)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Perfil de recomendacao nao encontrado: " + codigoPerfil + "."
                ));
    }

    private Optional<CpuModel> selecionarCpu(CriterioRecomendacao criterio) {
        Comparator<CpuModel> comparador = Comparator
                .comparing((CpuModel cpu) -> criterio.prefereGpuIntegrada() && !compatibilidadeService.temGpuIntegrada(cpu))
                .thenComparing(ofertaPrecoService::calcularPrecoPreferencial);
        return cpuService.listarTodos().stream()
                .filter(cpu -> cpu.getNucleos() >= criterio.nucleosCpuMinimos())
                .sorted(comparador)
                .findFirst();
    }

    private Optional<PlacaMaeModel> selecionarPlacaMae(CpuModel cpu) {
        return placaMaeService.listarTodos().stream()
                .filter(placaMae -> placaMae.getSocket().equalsIgnoreCase(cpu.getSocket()))
                .min(Comparator.comparing(ofertaPrecoService::calcularPrecoPreferencial));
    }

    private Optional<RamModel> selecionarRam(CriterioRecomendacao criterio, PlacaMaeModel placaMae) {
        String geracao = placaMae == null ? null : compatibilidadeService.inferirGeracaoRam(placaMae);
        return ramService.listarTodos().stream()
                .filter(ram -> ram.getMemoria() >= criterio.memoriaRamMinima())
                .filter(ram -> geracao == null || ram.getGeracao().equalsIgnoreCase(geracao))
                .min(Comparator.comparing(ofertaPrecoService::calcularPrecoPreferencial));
    }

    private Optional<GpuModel> selecionarGpu(CriterioRecomendacao criterio) {
        return gpuService.listarTodos().stream()
                .filter(gpu -> gpu.getMemoria() >= criterio.memoriaGpuMinima())
                .min(Comparator.comparing(ofertaPrecoService::calcularPrecoPreferencial));
    }

    private Optional<ArmazenamentoModel> selecionarArmazenamento(CriterioRecomendacao criterio) {
        return armazenamentoService.listarTodos().stream()
                .filter(armazenamento -> armazenamento.getMemoria() >= criterio.armazenamentoMinimo())
                .sorted(Comparator
                        .comparing((ArmazenamentoModel a) -> !"NVME".equalsIgnoreCase(a.getPadrao()))
                        .thenComparing(ofertaPrecoService::calcularPrecoPreferencial))
                .findFirst();
    }

    private Optional<FonteModel> selecionarFonte(CriterioRecomendacao criterio, CpuModel cpu, GpuModel gpu) {
        int potenciaMinima = criterio.potenciaFonteMinima();
        if (cpu != null) potenciaMinima = Math.max(potenciaMinima, cpu.getConsumo() + 100);
        if (cpu != null && gpu != null) potenciaMinima = Math.max(potenciaMinima, cpu.getConsumo() + gpu.getConsumo() + 150);
        int potenciaNecessaria = potenciaMinima;
        return fonteService.listarTodos().stream()
                .filter(fonte -> fonte.getPotencia() >= potenciaNecessaria)
                .min(Comparator.comparing(ofertaPrecoService::calcularPrecoPreferencial));
    }

    private Optional<MonitorModel> selecionarMonitor(CriterioRecomendacao criterio) {
        return monitorService.listarTodos().stream()
                .filter(monitor -> monitor.getFrequencia() >= criterio.frequenciaMonitorMinima())
                .filter(monitor -> criterio.resolucaoMonitor() == null || monitor.getResolucao().equalsIgnoreCase(criterio.resolucaoMonitor()))
                .min(Comparator.comparing(ofertaPrecoService::calcularPrecoPreferencial))
                .or(() -> monitorService.listarTodos().stream().min(Comparator.comparing(ofertaPrecoService::calcularPrecoPreferencial)));
    }

    private CriterioRecomendacao criterio(String codigoPerfil) {
        return switch (codigoPerfil.toLowerCase()) {
            case "trabalho"            -> new CriterioRecomendacao(false, true,  4, 8,  0,  256, 400, "1920x1080",  60);
            case "jogo-inicial"        -> new CriterioRecomendacao(true,  false, 4, 16, 4,  512, 500, "1920x1080",  75);
            case "jogo-intermediario"  -> new CriterioRecomendacao(true,  false, 6, 16, 8,  1000,650, "2560x1440", 120);
            case "jogos-pesados"       -> new CriterioRecomendacao(true,  false, 8, 32, 12, 1000,750, "3840x2160", 120);
            case "profissional"        -> new CriterioRecomendacao(true,  false, 12,32, 12, 1000,750, "3840x2160",  60);
            default                    -> new CriterioRecomendacao(false, false, 4, 8,  0,  256, 400, null,         60);
        };
    }

    private record CriterioRecomendacao(
            boolean exigeGpuDedicada,
            boolean prefereGpuIntegrada,
            int nucleosCpuMinimos,
            int memoriaRamMinima,
            int memoriaGpuMinima,
            int armazenamentoMinimo,
            int potenciaFonteMinima,
            String resolucaoMonitor,
            int frequenciaMonitorMinima
    ) {}
}
