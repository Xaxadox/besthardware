package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.UsuarioRequest;
import com.omni.besthardware.dtos.UsuarioResponse;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.UsuarioModel;
import com.omni.besthardware.services.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(required = false) String nome) {
        if (nome != null) {
            return toResponseList(usuarioService.buscarPorNome(nome));
        }

        return toResponseList(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id)
                .map(DtoMapper::toUsuarioResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email)
                .map(DtoMapper::toUsuarioResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/existe-email")
    public Map<String, Boolean> existePorEmail(@RequestParam String email) {
        return Map.of("existe", usuarioService.existePorEmail(email));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toUsuarioResponse(usuarioService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequest request) {
        return usuarioService.atualizar(id, toModel(request))
                .map(DtoMapper::toUsuarioResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!usuarioService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private List<UsuarioResponse> toResponseList(List<UsuarioModel> usuarios) {
        return usuarios.stream().map(DtoMapper::toUsuarioResponse).toList();
    }

    private UsuarioModel toModel(UsuarioRequest request) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        return usuario;
    }
}
