package com.omni.besthardware.rest.controller;

import com.omni.besthardware.rest.dto.request.ComponenteFiltroRequest;
import com.omni.besthardware.rest.dto.request.ComponenteRequest;
import com.omni.besthardware.rest.dto.response.ComponenteResponse;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.service.ComponenteService;
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
 * Controlador REST responsavel pelas operacoes de Componente no projeto BestHardware.
 */
@RestController
@RequestMapping("/api/componentes")
public class ComponenteController {

    private final ComponenteService componenteService;

    public ComponenteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(@ModelAttribute ComponenteFiltroRequest filtro) {
        return toResponseList(componenteService.buscarComFiltros(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(componenteService.buscarObrigatorio(id)));
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
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(
                componenteService.atualizarObrigatorio(id, toModel(request))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        componenteService.excluirObrigatorio(id);
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
