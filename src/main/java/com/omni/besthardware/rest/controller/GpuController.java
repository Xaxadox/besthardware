package com.omni.besthardware.rest.controller;

import com.omni.besthardware.rest.dto.response.ComponenteResponse;
import com.omni.besthardware.rest.dto.request.GpuFiltroRequest;
import com.omni.besthardware.rest.dto.request.GpuRequest;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.model.GpuModel;
import com.omni.besthardware.service.GpuService;
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

/**
 * Controlador REST responsavel pelas operacoes de Gpu no projeto BestHardware.
 */
@RestController
@RequestMapping("/api/gpus")
public class GpuController {

    private final GpuService gpuService;

    public GpuController(GpuService gpuService) {
        this.gpuService = gpuService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(@ModelAttribute GpuFiltroRequest filtro) {
        return toResponseList(gpuService.buscarComFiltros(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(gpuService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody GpuRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(gpuService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody GpuRequest request) {
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(gpuService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        gpuService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }

    private List<ComponenteResponse> toResponseList(List<GpuModel> gpus) {
        return gpus.stream().map(DtoMapper::toComponenteResponse).toList();
    }

    private GpuModel toModel(GpuRequest request) {
        GpuModel gpu = new GpuModel();
        gpu.setTipo(request.tipo());
        gpu.setPreco(request.preco());
        gpu.setModelo(request.modelo());
        gpu.setMemoria(request.memoria());
        gpu.setConsumo(request.consumo());
        gpu.setMarca(request.marca());
        return gpu;
    }
}
