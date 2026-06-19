package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.PerfilMapper;
import com.omni.besthardware.rest.dto.request.PerfilRequest;
import com.omni.besthardware.rest.dto.response.PerfilResponse;
import com.omni.besthardware.service.PerfilService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final PerfilService perfilService;
    private final PerfilMapper perfilMapper;

    public PerfilController(PerfilService perfilService, PerfilMapper perfilMapper) {
        this.perfilService = perfilService;
        this.perfilMapper = perfilMapper;
    }

    @GetMapping
    public List<PerfilResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer componenteId
    ) {
        List<?> perfis;
        if (nome != null) {
            perfis = perfilService.buscarPorNome(nome);
        } else if (componenteId != null) {
            perfis = perfilService.buscarPorComponente(componenteId);
        } else {
            perfis = perfilService.listarTodos();
        }
        return ((List<com.omni.besthardware.model.PerfilModel>) perfis).stream()
                .map(perfilMapper::toPerfilResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(perfilMapper.toPerfilResponse(perfilService.buscarObrigatorio(id)));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PerfilResponse> buscarPorNomeExato(@PathVariable String nome) {
        return ResponseEntity.ok(perfilMapper.toPerfilResponse(perfilService.buscarPorNomeExatoObrigatorio(nome)));
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> criar(@Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(perfilMapper.toPerfilResponse(perfilService.criar(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.ok(perfilMapper.toPerfilResponse(perfilService.atualizarObrigatorio(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        perfilService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }
}
