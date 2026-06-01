package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ComponenteRequest;
import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.ComponenteModel;
import com.omni.besthardware.services.ComponenteService;
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
@RequestMapping("/api/componentes")
public class ComponenteController {

    private final ComponenteService componenteService;

    public ComponenteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) Integer perfilId,
            @RequestParam(required = false) Integer componenteCompativelId
    ) {
        if (tipo != null) {
            return toResponseList(componenteService.buscarPorTipo(tipo));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(componenteService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        if (perfilId != null) {
            return toResponseList(componenteService.buscarPorPerfil(perfilId));
        }

        if (componenteCompativelId != null) {
            return toResponseList(componenteService.buscarCompativeisCom(componenteCompativelId));
        }

        return toResponseList(componenteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return componenteService.buscarPorId(id)
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody ComponenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(componenteService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ComponenteRequest request
    ) {
        return componenteService.atualizar(id, toModel(request))
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!componenteService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private List<ComponenteResponse> toResponseList(List<ComponenteModel> componentes) {
        return componentes.stream().map(DtoMapper::toComponenteResponse).toList();
    }

    private ComponenteModel toModel(ComponenteRequest request) {
        ComponenteModel componente = new ComponenteModel();
        componente.setTipo(request.tipo());
        componente.setPreco(request.preco());
        return componente;
    }
}
