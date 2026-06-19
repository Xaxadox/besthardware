package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.UsuarioMapper;
import com.omni.besthardware.model.UsuarioModel;
import com.omni.besthardware.rest.dto.request.UsuarioRequest;
import com.omni.besthardware.rest.dto.response.UsuarioResponse;
import com.omni.besthardware.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(required = false) String nome) {
        List<UsuarioModel> usuarios = nome != null
                ? usuarioService.buscarPorNome(nome)
                : usuarioService.listarTodos();
        return usuarios.stream().map(usuarioMapper::toUsuarioResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioMapper.toUsuarioResponse(usuarioService.buscarObrigatorio(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioMapper.toUsuarioResponse(usuarioService.buscarPorEmailObrigatorio(email)));
    }

    @GetMapping("/existe-email")
    public Map<String, Boolean> existePorEmail(@RequestParam String email) {
        return Map.of("existe", usuarioService.existePorEmail(email));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioMapper.toUsuarioResponse(usuarioService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioMapper.toUsuarioResponse(usuarioService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        usuarioService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioModel toModel(UsuarioRequest request) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        return usuario;
    }
}
