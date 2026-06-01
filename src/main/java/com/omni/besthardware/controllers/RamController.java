package com.omni.besthardware.controllers;

import com.omni.besthardware.models.RamModel;
import com.omni.besthardware.services.RamService;
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
@RequestMapping("/api/rams")
public class RamController {

    private final RamService ramService;

    public RamController(RamService ramService) {
        this.ramService = ramService;
    }

    @GetMapping
    public List<RamModel> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String geracao,
            @RequestParam(required = false) Integer frequenciaMinima,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer memoriaMinima
    ) {
        if (tipo != null) {
            return ramService.buscarPorTipo(tipo);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return ramService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        if (geracao != null) {
            return ramService.buscarPorGeracao(geracao);
        }

        if (frequenciaMinima != null) {
            return ramService.buscarPorFrequenciaMinima(frequenciaMinima);
        }

        if (marca != null) {
            return ramService.buscarPorMarca(marca);
        }

        if (memoriaMinima != null) {
            return ramService.buscarPorMemoriaMinima(memoriaMinima);
        }

        return ramService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RamModel> buscarPorId(@PathVariable Integer id) {
        return ramService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RamModel> criar(@Valid @RequestBody RamModel ram) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ramService.salvar(ram));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RamModel> atualizar(@PathVariable Integer id, @Valid @RequestBody RamModel ram) {
        return ramService.atualizar(id, ram)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!ramService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

