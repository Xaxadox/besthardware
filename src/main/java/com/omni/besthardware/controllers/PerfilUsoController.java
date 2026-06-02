package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.PerfilUsoResponse;
import com.omni.besthardware.services.PerfilUsoService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perfis/escopos")
public class PerfilUsoController {

    private final PerfilUsoService perfilUsoService;

    public PerfilUsoController(PerfilUsoService perfilUsoService) {
        this.perfilUsoService = perfilUsoService;
    }

    @GetMapping
    public List<PerfilUsoResponse> listar() {
        return perfilUsoService.listarTodos();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<PerfilUsoResponse> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(perfilUsoService.buscarPorCodigoObrigatorio(codigo));
    }
}
