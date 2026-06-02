package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.PerfilRequest;
import com.omni.besthardware.dtos.PerfilResponse;
import com.omni.besthardware.dtos.PerfilUsoResponse;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.PerfilModel;
import com.omni.besthardware.services.PerfilService;
import com.omni.besthardware.services.PerfilUsoService;
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

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final PerfilService perfilService;
    private final PerfilUsoService perfilUsoService;

    public PerfilController(
            PerfilService perfilService,
            PerfilUsoService perfilUsoService
    ) {
        this.perfilService = perfilService;
        this.perfilUsoService = perfilUsoService;
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

    @GetMapping("/escopos")
    public List<PerfilUsoResponse> listarEscopos() {
        return perfilUsoService.listarTodos();
    }

    @GetMapping("/escopos/{codigo}")
    public ResponseEntity<PerfilUsoResponse> buscarEscopoPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(perfilUsoService.buscarPorCodigoObrigatorio(codigo));
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
