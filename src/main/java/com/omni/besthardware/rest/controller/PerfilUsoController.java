package com.omni.besthardware.rest.controller;

import com.omni.besthardware.rest.dto.response.PerfilUsoResponse;
import com.omni.besthardware.service.PerfilUsoService;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
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
    public ResponseEntity<PerfilUsoResponse> buscarPorCodigo(@PathVariable @NotBlank String codigo) {
        return ResponseEntity.ok(perfilUsoService.buscarPorCodigoObrigatorio(codigo));
    }
}
