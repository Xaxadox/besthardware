package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.GpuModel;
import com.omni.besthardware.rest.dto.request.GpuFiltroRequest;
import com.omni.besthardware.rest.dto.request.GpuRequest;
import com.omni.besthardware.rest.dto.response.GpuResponse;
import com.omni.besthardware.service.GpuService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/gpus")
public class GpuController {

    private final GpuService gpuService;
    private final ComponenteMapper componenteMapper;

    public GpuController(GpuService gpuService, ComponenteMapper componenteMapper) {
        this.gpuService = gpuService;
        this.componenteMapper = componenteMapper;
    }

    @GetMapping
    public List<GpuResponse> listar(@Valid @ModelAttribute GpuFiltroRequest filtro) {
        return gpuService.buscarComFiltros(filtro).stream()
                .map(componenteMapper::toGpuResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GpuResponse> buscarPorId(@PathVariable @Positive Integer id) {
        return ResponseEntity.ok(componenteMapper.toGpuResponse(gpuService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<GpuResponse> criar(@Valid @RequestBody GpuRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteMapper.toGpuResponse(gpuService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GpuResponse> atualizar(@PathVariable @Positive Integer id, @Valid @RequestBody GpuRequest request) {
        return ResponseEntity.ok(componenteMapper.toGpuResponse(gpuService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive Integer id) {
        gpuService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }

    private GpuModel toModel(GpuRequest request) {
        GpuModel gpu = new GpuModel();
        gpu.setTipo(request.tipo());
        gpu.setPreco(request.preco());
        gpu.setModelo(request.modelo());
        gpu.setMarca(request.marca());
        gpu.setMemoria(request.memoria());
        gpu.setConsumo(request.consumo());
        return gpu;
    }
}
