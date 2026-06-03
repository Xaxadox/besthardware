package com.omni.besthardware.rest.controller;

import com.omni.besthardware.rest.dto.request.PerfilRequest;
import com.omni.besthardware.rest.dto.response.PerfilResponse;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.model.PerfilModel;
import com.omni.besthardware.service.PerfilService;
import jakarta.validation.Valid;
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

/**
 * Controlador REST responsavel pelas operacoes de Perfil no projeto BestHardware.
 */
@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public List<PerfilResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer componenteId
    ) {
        if (nome != null) {
            return toResponseList(perfilService.buscarPorNome(nome));
        }

        if (componenteId != null) {
            return toResponseList(perfilService.buscarPorComponente(componenteId));
        }

        return toResponseList(perfilService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(DtoMapper.toPerfilResponse(perfilService.buscarObrigatorio(id)));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PerfilResponse> buscarPorNomeExato(@PathVariable String nome) {
        return ResponseEntity.ok(DtoMapper.toPerfilResponse(perfilService.buscarPorNomeExatoObrigatorio(nome)));
    }

    @PostMapping
    public ResponseEntity<PerfilResponse> criar(@Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toPerfilResponse(perfilService.criar(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody PerfilRequest request) {
        return ResponseEntity.ok(DtoMapper.toPerfilResponse(perfilService.atualizarObrigatorio(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        perfilService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }

    private List<PerfilResponse> toResponseList(List<PerfilModel> perfis) {
        return perfis.stream().map(DtoMapper::toPerfilResponse).toList();
    }
}
