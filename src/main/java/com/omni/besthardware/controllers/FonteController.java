package com.omni.besthardware.controllers;

import com.omni.besthardware.models.FonteModel;
import com.omni.besthardware.services.FonteService;
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
@RequestMapping("/api/fontes")
public class FonteController {

    private final FonteService fonteService;

    public FonteController(FonteService fonteService) {
        this.fonteService = fonteService;
    }

    @GetMapping
    public List<FonteModel> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer potenciaMinima,
            @RequestParam(required = false) String certificacao
    ) {
        if (tipo != null) {
            return fonteService.buscarPorTipo(tipo);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return fonteService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        if (marca != null) {
            return fonteService.buscarPorMarca(marca);
        }

        if (potenciaMinima != null) {
            return fonteService.buscarPorPotenciaMinima(potenciaMinima);
        }

        if (certificacao != null) {
            return fonteService.buscarPorCertificacao(certificacao);
        }

        return fonteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FonteModel> buscarPorId(@PathVariable Integer id) {
        return fonteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FonteModel> criar(@Valid @RequestBody FonteModel fonte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fonteService.salvar(fonte));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FonteModel> atualizar(@PathVariable Integer id, @Valid @RequestBody FonteModel fonte) {
        return fonteService.atualizar(id, fonte)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!fonteService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

