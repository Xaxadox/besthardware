package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.OfertaPrecoFiltroRequest;
import com.omni.besthardware.dtos.OfertaPrecoRequest;
import com.omni.besthardware.dtos.OfertaPrecoResponse;
import com.omni.besthardware.services.OfertaPrecoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ofertas-preco")
public class OfertaPrecoController {

    private final OfertaPrecoService ofertaPrecoService;

    public OfertaPrecoController(OfertaPrecoService ofertaPrecoService) {
        this.ofertaPrecoService = ofertaPrecoService;
    }

    @GetMapping
    public List<OfertaPrecoResponse> listar(@ModelAttribute OfertaPrecoFiltroRequest filtro) {
        return ofertaPrecoService.listarRespostas(filtro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaPrecoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ofertaPrecoService.buscarRespostaPorId(id));
    }

    @GetMapping("/componentes/{componenteId}/menor-preco")
    public ResponseEntity<OfertaPrecoResponse> buscarMenorOfertaPorComponente(@PathVariable Integer componenteId) {
        return ResponseEntity.ok(ofertaPrecoService.buscarMenorOfertaResposta(componenteId));
    }

    @PostMapping
    public ResponseEntity<OfertaPrecoResponse> criar(@Valid @RequestBody OfertaPrecoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ofertaPrecoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfertaPrecoResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody OfertaPrecoRequest request
    ) {
        return ResponseEntity.ok(ofertaPrecoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        ofertaPrecoService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }
}
