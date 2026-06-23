package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.rest.dto.request.ComponenteFiltroRequest;
import com.omni.besthardware.rest.dto.request.ComponenteRequest;
import com.omni.besthardware.rest.dto.response.ComponenteResponse;
import com.omni.besthardware.service.ComponenteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/componentes")
public class ComponenteController {

    private final ComponenteService componenteService;
    private final ComponenteMapper componenteMapper;

    public ComponenteController(ComponenteService componenteService, ComponenteMapper componenteMapper) {
        this.componenteService = componenteService;
        this.componenteMapper = componenteMapper;
    }

    @GetMapping
    public List<ComponenteResponse> listar(@Valid @ModelAttribute ComponenteFiltroRequest filtro) {
        return componenteService.buscarComFiltros(filtro).stream()
                .map(componenteMapper::toComponenteResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable @Positive Integer id) {
        return ResponseEntity.ok(componenteMapper.toComponenteResponse(componenteService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody ComponenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteMapper.toComponenteResponse(componenteService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(
            @PathVariable @Positive Integer id,
            @Valid @RequestBody ComponenteRequest request
    ) {
        return ResponseEntity.ok(componenteMapper.toComponenteResponse(
                componenteService.atualizarObrigatorio(id, toModel(request))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive Integer id) {
        componenteService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }

    private ComponenteModel toModel(ComponenteRequest request) {
        ComponenteModel componente = new ComponenteModel();
        componente.setTipo(request.tipo());
        componente.setPreco(request.preco());
        return componente;
    }
}
