package com.omni.besthardware.rest.controller;

import com.omni.besthardware.rest.dto.request.ItemOrcamentoCadastroRequest;
import com.omni.besthardware.rest.dto.request.ItemOrcamentoFiltroRequest;
import com.omni.besthardware.rest.dto.response.ItemOrcamentoResponse;
import com.omni.besthardware.service.ItemOrcamentoService;
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
@RequestMapping("/api/itens-orcamento")
public class ItemOrcamentoController {

    private final ItemOrcamentoService itemOrcamentoService;

    public ItemOrcamentoController(ItemOrcamentoService itemOrcamentoService) {
        this.itemOrcamentoService = itemOrcamentoService;
    }

    @GetMapping
    public List<ItemOrcamentoResponse> listar(@Valid @ModelAttribute ItemOrcamentoFiltroRequest filtro) {
        return itemOrcamentoService.listarRespostas(filtro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemOrcamentoResponse> buscarPorId(@PathVariable @Positive Integer id) {
        return ResponseEntity.ok(itemOrcamentoService.buscarRespostaPorId(id));
    }

    @PostMapping
    public ResponseEntity<ItemOrcamentoResponse> criar(@Valid @RequestBody ItemOrcamentoCadastroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemOrcamentoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemOrcamentoResponse> atualizar(
            @PathVariable @Positive Integer id,
            @Valid @RequestBody ItemOrcamentoCadastroRequest request
    ) {
        return ResponseEntity.ok(itemOrcamentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive Integer id) {
        itemOrcamentoService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }
}

