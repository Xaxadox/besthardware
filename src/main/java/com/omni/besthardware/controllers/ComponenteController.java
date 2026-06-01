package com.omni.besthardware.controllers;

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
    public List<ComponenteModel> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) Integer perfilId,
            @RequestParam(required = false) Integer componenteCompativelId
    ) {
        if (tipo != null) {
            return componenteService.buscarPorTipo(tipo);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return componenteService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        if (perfilId != null) {
            return componenteService.buscarPorPerfil(perfilId);
        }

        if (componenteCompativelId != null) {
            return componenteService.buscarCompativeisCom(componenteCompativelId);
        }

        return componenteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteModel> buscarPorId(@PathVariable Integer id) {
        return componenteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComponenteModel> criar(@Valid @RequestBody ComponenteModel componente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(componenteService.salvar(componente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteModel> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ComponenteModel componente
    ) {
        return componenteService.atualizar(id, componente)
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
}

