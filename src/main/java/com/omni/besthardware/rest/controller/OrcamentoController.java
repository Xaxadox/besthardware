package com.omni.besthardware.rest.controller;

import com.omni.besthardware.rest.dto.request.OrcamentoAtualizacaoRequest;
import com.omni.besthardware.rest.dto.request.OrcamentoFiltroRequest;
import com.omni.besthardware.rest.dto.request.OrcamentoRequest;
import com.omni.besthardware.rest.dto.response.OrcamentoResponse;
import com.omni.besthardware.service.OrcamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    @GetMapping
    public List<OrcamentoResponse> listar(@Valid @ModelAttribute OrcamentoFiltroRequest filtro) {
        return orcamentoService.listarRespostas(filtro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> buscarPorId(@PathVariable @Positive Integer id) {
        return ResponseEntity.ok(orcamentoService.buscarRespostaPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamentoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> atualizar(
            @PathVariable @Positive Integer id,
            @Valid @RequestBody OrcamentoAtualizacaoRequest request
    ) {
        return ResponseEntity.ok(orcamentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive Integer id) {
        orcamentoService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }
}

