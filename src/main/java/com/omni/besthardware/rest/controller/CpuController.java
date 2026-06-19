package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.CpuModel;
import com.omni.besthardware.rest.dto.request.CpuFiltroRequest;
import com.omni.besthardware.rest.dto.request.CpuRequest;
import com.omni.besthardware.rest.dto.response.CpuResponse;
import com.omni.besthardware.service.CpuService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cpus")
public class CpuController {

    private final CpuService cpuService;
    private final ComponenteMapper componenteMapper;

    public CpuController(CpuService cpuService, ComponenteMapper componenteMapper) {
        this.cpuService = cpuService;
        this.componenteMapper = componenteMapper;
    }

    @GetMapping
    public List<CpuResponse> listar(@ModelAttribute CpuFiltroRequest filtro) {
        return cpuService.buscarComFiltros(filtro).stream()
                .map(componenteMapper::toCpuResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CpuResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(componenteMapper.toCpuResponse(cpuService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<CpuResponse> criar(@Valid @RequestBody CpuRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteMapper.toCpuResponse(cpuService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CpuResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody CpuRequest request) {
        return ResponseEntity.ok(componenteMapper.toCpuResponse(cpuService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        cpuService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }

    private CpuModel toModel(CpuRequest request) {
        CpuModel cpu = new CpuModel();
        cpu.setTipo(request.tipo());
        cpu.setPreco(request.preco());
        cpu.setModelo(request.modelo());
        cpu.setSocket(request.socket());
        cpu.setFrequencia(request.frequencia());
        cpu.setConsumo(request.consumo());
        cpu.setAnoLancamento(request.anoLancamento());
        cpu.setNucleos(request.nucleos());
        return cpu;
    }
}
