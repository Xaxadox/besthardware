package com.omni.besthardware.controllers;

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
    public List<CpuModel> listar(
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
            return cpuService.buscarPorTipo(tipo);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return cpuService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        if (modelo != null) {
            return cpuService.buscarPorModelo(modelo);
        }

        if (socket != null) {
            return cpuService.buscarPorSocket(socket);
        }

        if (frequenciaMinima != null) {
            return cpuService.buscarPorFrequenciaMinima(frequenciaMinima);
        }

        if (consumoMaximo != null) {
            return cpuService.buscarPorConsumoMaximo(consumoMaximo);
        }

        if (nucleosMinimos != null) {
            return cpuService.buscarPorNucleosMinimos(nucleosMinimos);
        }

        if (dataInicial != null && dataFinal != null) {
            return cpuService.buscarPorPeriodoLancamento(dataInicial, dataFinal);
        }

        return cpuService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuModel> buscarPorId(@PathVariable Integer id) {
        return cpuService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CpuModel> criar(@Valid @RequestBody CpuModel cpu) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cpuService.salvar(cpu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CpuModel> atualizar(@PathVariable Integer id, @Valid @RequestBody CpuModel cpu) {
        return cpuService.atualizar(id, cpu)
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
}

