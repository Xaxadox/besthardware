package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.dtos.MonitorRequest;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.mappers.DtoMapper;
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
    public List<ComponenteResponse> listar(
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
            return toResponseList(monitorService.buscarPorTipo(tipo));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(monitorService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        if (marca != null) {
            return toResponseList(monitorService.buscarPorMarca(marca));
        }

        if (tamanho != null) {
            return toResponseList(monitorService.buscarPorTamanho(tamanho));
        }

        if (resolucao != null) {
            return toResponseList(monitorService.buscarPorResolucao(resolucao));
        }

        if (frequenciaMinima != null) {
            return toResponseList(monitorService.buscarPorFrequenciaMinima(frequenciaMinima));
        }

        if (tecnologia != null) {
            return toResponseList(monitorService.buscarPorTecnologia(tecnologia));
        }

        return toResponseList(monitorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return monitorService.buscarPorId(id)
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Monitor", id));
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody MonitorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(monitorService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody MonitorRequest request) {
        return monitorService.atualizar(id, toModel(request))
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Monitor", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!monitorService.excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Monitor", id);
        }

        return ResponseEntity.noContent().build();
    }

    private List<ComponenteResponse> toResponseList(List<MonitorModel> monitores) {
        return monitores.stream().map(DtoMapper::toComponenteResponse).toList();
    }

    private MonitorModel toModel(MonitorRequest request) {
        MonitorModel monitor = new MonitorModel();
        monitor.setTipo(request.tipo());
        monitor.setPreco(request.preco());
        monitor.setMarca(request.marca());
        monitor.setTamanho(request.tamanho());
        monitor.setResolucao(request.resolucao());
        monitor.setFrequencia(request.frequencia());
        monitor.setTecnologia(request.tecnologia());
        return monitor;
    }
}
