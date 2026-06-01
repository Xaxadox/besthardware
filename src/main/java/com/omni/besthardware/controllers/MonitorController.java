package com.omni.besthardware.controllers;

import com.omni.besthardware.models.MonitorModel;
import com.omni.besthardware.services.MonitorService;
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
@RequestMapping("/api/monitores")
public class MonitorController {

    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @GetMapping
    public List<MonitorModel> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String tamanho,
            @RequestParam(required = false) String resolucao,
            @RequestParam(required = false) Integer frequenciaMinima,
            @RequestParam(required = false) String tecnologia
    ) {
        if (tipo != null) {
            return monitorService.buscarPorTipo(tipo);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return monitorService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        if (marca != null) {
            return monitorService.buscarPorMarca(marca);
        }

        if (tamanho != null) {
            return monitorService.buscarPorTamanho(tamanho);
        }

        if (resolucao != null) {
            return monitorService.buscarPorResolucao(resolucao);
        }

        if (frequenciaMinima != null) {
            return monitorService.buscarPorFrequenciaMinima(frequenciaMinima);
        }

        if (tecnologia != null) {
            return monitorService.buscarPorTecnologia(tecnologia);
        }

        return monitorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitorModel> buscarPorId(@PathVariable Integer id) {
        return monitorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MonitorModel> criar(@Valid @RequestBody MonitorModel monitor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(monitorService.salvar(monitor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitorModel> atualizar(@PathVariable Integer id, @Valid @RequestBody MonitorModel monitor) {
        return monitorService.atualizar(id, monitor)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!monitorService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

