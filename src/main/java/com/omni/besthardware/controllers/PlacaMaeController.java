package com.omni.besthardware.controllers;

import com.omni.besthardware.models.PlacaMaeModel;
import com.omni.besthardware.services.PlacaMaeService;
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
@RequestMapping("/api/placas-mae")
public class PlacaMaeController {

    private final PlacaMaeService placaMaeService;

    public PlacaMaeController(PlacaMaeService placaMaeService) {
        this.placaMaeService = placaMaeService;
    }

    @GetMapping
    public List<PlacaMaeModel> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String socket,
            @RequestParam(required = false) String chipset,
            @RequestParam(required = false) String formato
    ) {
        if (tipo != null) {
            return placaMaeService.buscarPorTipo(tipo);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return placaMaeService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        if (marca != null) {
            return placaMaeService.buscarPorMarca(marca);
        }

        if (socket != null) {
            return placaMaeService.buscarPorSocket(socket);
        }

        if (chipset != null) {
            return placaMaeService.buscarPorChipset(chipset);
        }

        if (formato != null) {
            return placaMaeService.buscarPorFormato(formato);
        }

        return placaMaeService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacaMaeModel> buscarPorId(@PathVariable Integer id) {
        return placaMaeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlacaMaeModel> criar(@Valid @RequestBody PlacaMaeModel placaMae) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placaMaeService.salvar(placaMae));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlacaMaeModel> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PlacaMaeModel placaMae
    ) {
        return placaMaeService.atualizar(id, placaMae)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!placaMaeService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

