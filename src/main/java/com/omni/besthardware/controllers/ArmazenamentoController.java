package com.omni.besthardware.controllers;

import com.omni.besthardware.models.ArmazenamentoModel;
import com.omni.besthardware.services.ArmazenamentoService;
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
@RequestMapping("/api/armazenamentos")
public class ArmazenamentoController {

    private final ArmazenamentoService armazenamentoService;

    public ArmazenamentoController(ArmazenamentoService armazenamentoService) {
        this.armazenamentoService = armazenamentoService;
    }

    @GetMapping
    public List<ArmazenamentoModel> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String tecnologia,
            @RequestParam(required = false) String padrao,
            @RequestParam(required = false) Integer memoriaMinima,
            @RequestParam(required = false) Integer velocidadeLeituraMinima,
            @RequestParam(required = false) Integer velocidadeEscritaMinima
    ) {
        if (tipo != null) {
            return armazenamentoService.buscarPorTipo(tipo);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return armazenamentoService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        if (tecnologia != null) {
            return armazenamentoService.buscarPorTecnologia(tecnologia);
        }

        if (padrao != null) {
            return armazenamentoService.buscarPorPadrao(padrao);
        }

        if (memoriaMinima != null) {
            return armazenamentoService.buscarPorMemoriaMinima(memoriaMinima);
        }

        if (velocidadeLeituraMinima != null) {
            return armazenamentoService.buscarPorVelocidadeLeituraMinima(velocidadeLeituraMinima);
        }

        if (velocidadeEscritaMinima != null) {
            return armazenamentoService.buscarPorVelocidadeEscritaMinima(velocidadeEscritaMinima);
        }

        return armazenamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArmazenamentoModel> buscarPorId(@PathVariable Integer id) {
        return armazenamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArmazenamentoModel> criar(@Valid @RequestBody ArmazenamentoModel armazenamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(armazenamentoService.salvar(armazenamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArmazenamentoModel> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ArmazenamentoModel armazenamento
    ) {
        return armazenamentoService.atualizar(id, armazenamento)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!armazenamentoService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

