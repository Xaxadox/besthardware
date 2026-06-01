package com.omni.besthardware.controllers;

import com.omni.besthardware.models.GpuModel;
import com.omni.besthardware.services.GpuService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
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
@RequestMapping("/api/gpus")
public class GpuController {

    private final GpuService gpuService;

    public GpuController(GpuService gpuService) {
        this.gpuService = gpuService;
    }

    @GetMapping
    public List<GpuModel> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer memoriaMinima,
            @RequestParam(required = false) Integer consumoMaximo
    ) {
        if (tipo != null) {
            return gpuService.buscarPorTipo(tipo);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return gpuService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        if (modelo != null) {
            return gpuService.buscarPorModelo(modelo);
        }

        if (marca != null) {
            return gpuService.buscarPorMarca(marca);
        }

        if (memoriaMinima != null) {
            return gpuService.buscarPorMemoriaMinima(memoriaMinima);
        }

        if (consumoMaximo != null) {
            return gpuService.buscarPorConsumoMaximo(consumoMaximo);
        }

        return gpuService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GpuModel> buscarPorId(@PathVariable Integer id) {
        return gpuService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GpuModel> criar(@Valid @RequestBody GpuModel gpu) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gpuService.salvar(gpu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GpuModel> atualizar(@PathVariable Integer id, @Valid @RequestBody GpuModel gpu) {
        return gpuService.atualizar(id, gpu)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!gpuService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

