package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.MonitorModel;
import com.omni.besthardware.rest.dto.request.MonitorFiltroRequest;
import com.omni.besthardware.rest.dto.request.MonitorRequest;
import com.omni.besthardware.rest.dto.response.MonitorResponse;
import com.omni.besthardware.service.MonitorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monitores")
public class MonitorController {

    private final MonitorService monitorService;
    private final ComponenteMapper componenteMapper;

    public MonitorController(MonitorService monitorService, ComponenteMapper componenteMapper) {
        this.monitorService = monitorService;
        this.componenteMapper = componenteMapper;
    }

    @GetMapping
    public List<MonitorResponse> listar(@ModelAttribute MonitorFiltroRequest filtro) {
        return monitorService.buscarComFiltros(filtro).stream()
                .map(componenteMapper::toMonitorResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitorResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(componenteMapper.toMonitorResponse(monitorService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<MonitorResponse> criar(@Valid @RequestBody MonitorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteMapper.toMonitorResponse(monitorService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitorResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody MonitorRequest request) {
        return ResponseEntity.ok(componenteMapper.toMonitorResponse(monitorService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        monitorService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
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
