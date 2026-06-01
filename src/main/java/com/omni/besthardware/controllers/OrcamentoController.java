package com.omni.besthardware.controllers;

import com.omni.besthardware.models.OrcamentoModel;
import com.omni.besthardware.services.OrcamentoService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    @GetMapping
    public List<OrcamentoModel> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) Integer perfilId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo
    ) {
        if (nome != null) {
            return orcamentoService.buscarPorNome(nome);
        }

        if (usuarioId != null) {
            return orcamentoService.buscarPorUsuario(usuarioId);
        }

        if (perfilId != null) {
            return orcamentoService.buscarPorPerfil(perfilId);
        }

        if (dataInicial != null && dataFinal != null) {
            return orcamentoService.buscarPorPeriodoCriacao(dataInicial, dataFinal);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return orcamentoService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        return orcamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoModel> buscarPorId(@PathVariable Integer id) {
        return orcamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrcamentoModel> criar(@Valid @RequestBody OrcamentoModel orcamento) {
        if (orcamento.getDataCriacao() == null) {
            orcamento.setDataCriacao(LocalDateTime.now());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orcamentoService.salvar(orcamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoModel> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody OrcamentoModel orcamento
    ) {
        return orcamentoService.atualizar(id, orcamento)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!orcamentoService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

