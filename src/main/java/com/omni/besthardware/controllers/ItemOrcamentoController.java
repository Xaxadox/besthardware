package com.omni.besthardware.controllers;

import com.omni.besthardware.models.ItemOrcamentoModel;
import com.omni.besthardware.services.ItemOrcamentoService;
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
@RequestMapping("/api/itens-orcamento")
public class ItemOrcamentoController {

    private final ItemOrcamentoService itemOrcamentoService;

    public ItemOrcamentoController(ItemOrcamentoService itemOrcamentoService) {
        this.itemOrcamentoService = itemOrcamentoService;
    }

    @GetMapping
    public List<ItemOrcamentoModel> listar(
            @RequestParam(required = false) Integer orcamentoId,
            @RequestParam(required = false) Integer componenteId,
            @RequestParam(required = false) Integer quantidadeMinima,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo
    ) {
        if (orcamentoId != null && componenteId != null) {
            return itemOrcamentoService.buscarPorOrcamentoEComponente(orcamentoId, componenteId);
        }

        if (orcamentoId != null) {
            return itemOrcamentoService.buscarPorOrcamento(orcamentoId);
        }

        if (componenteId != null) {
            return itemOrcamentoService.buscarPorComponente(componenteId);
        }

        if (quantidadeMinima != null) {
            return itemOrcamentoService.buscarPorQuantidadeMinima(quantidadeMinima);
        }

        if (precoMinimo != null && precoMaximo != null) {
            return itemOrcamentoService.buscarPorFaixaDePreco(precoMinimo, precoMaximo);
        }

        return itemOrcamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemOrcamentoModel> buscarPorId(@PathVariable Integer id) {
        return itemOrcamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ItemOrcamentoModel> criar(@Valid @RequestBody ItemOrcamentoModel itemOrcamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemOrcamentoService.salvar(itemOrcamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemOrcamentoModel> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ItemOrcamentoModel itemOrcamento
    ) {
        return itemOrcamentoService.atualizar(id, itemOrcamento)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!itemOrcamentoService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}

