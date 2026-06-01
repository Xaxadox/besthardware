package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.dtos.CpuFiltroRequest;
import com.omni.besthardware.dtos.CpuRequest;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.CpuModel;
import com.omni.besthardware.services.CpuService;
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

    public CpuController(CpuService cpuService) {
        this.cpuService = cpuService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(@ModelAttribute CpuFiltroRequest filtro) {
        return toResponseList(cpuService.buscarComFiltros(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return cpuService.buscarPorId(id)
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("CPU", id));
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
                .orElseThrow(() -> new RecursoNaoEncontradoException("CPU", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!cpuService.excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("CPU", id);
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
