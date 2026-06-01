package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.dtos.CpuRequest;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.CpuModel;
import com.omni.besthardware.services.CpuService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cpus")
public class CpuController {

    private final CpuService cpuService;

    public CpuController(CpuService cpuService) {
        this.cpuService = cpuService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String socket,
            @RequestParam(required = false) Integer frequenciaMinima,
            @RequestParam(required = false) Integer consumoMaximo,
            @RequestParam(required = false) Integer nucleosMinimos,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal
    ) {
        if (tipo != null) {
            return toResponseList(cpuService.buscarPorTipo(tipo));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(cpuService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        if (modelo != null) {
            return toResponseList(cpuService.buscarPorModelo(modelo));
        }

        if (socket != null) {
            return toResponseList(cpuService.buscarPorSocket(socket));
        }

        if (frequenciaMinima != null) {
            return toResponseList(cpuService.buscarPorFrequenciaMinima(frequenciaMinima));
        }

        if (consumoMaximo != null) {
            return toResponseList(cpuService.buscarPorConsumoMaximo(consumoMaximo));
        }

        if (nucleosMinimos != null) {
            return toResponseList(cpuService.buscarPorNucleosMinimos(nucleosMinimos));
        }

        if (dataInicial != null && dataFinal != null) {
            return toResponseList(cpuService.buscarPorPeriodoLancamento(dataInicial, dataFinal));
        }

        return toResponseList(cpuService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return cpuService.buscarPorId(id)
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody CpuRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(cpuService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody CpuRequest request) {
        return cpuService.atualizar(id, toModel(request))
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!cpuService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private List<ComponenteResponse> toResponseList(List<CpuModel> cpus) {
        return cpus.stream().map(DtoMapper::toComponenteResponse).toList();
    }

    private CpuModel toModel(CpuRequest request) {
        CpuModel cpu = new CpuModel();
        cpu.setTipo(request.tipo());
        cpu.setPreco(request.preco());
        cpu.setModelo(request.modelo());
        cpu.setFrequencia(request.frequencia());
        cpu.setConsumo(request.consumo());
        cpu.setAnoLancamento(request.anoLancamento());
        cpu.setNucleos(request.nucleos());
        cpu.setSocket(request.socket());
        return cpu;
    }
}
