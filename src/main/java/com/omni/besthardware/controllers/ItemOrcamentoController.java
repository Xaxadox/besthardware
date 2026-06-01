package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ItemOrcamentoCadastroRequest;
import com.omni.besthardware.dtos.ItemOrcamentoResponse;
import com.omni.besthardware.models.ComponenteModel;
import com.omni.besthardware.models.ItemOrcamentoModel;
import com.omni.besthardware.models.OrcamentoModel;
import com.omni.besthardware.services.ComponenteService;
import com.omni.besthardware.services.ItemOrcamentoService;
import com.omni.besthardware.services.OrcamentoService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
    private final OrcamentoService orcamentoService;
    private final ComponenteService componenteService;

    public ItemOrcamentoController(
            ItemOrcamentoService itemOrcamentoService,
            OrcamentoService orcamentoService,
            ComponenteService componenteService
    ) {
        this.itemOrcamentoService = itemOrcamentoService;
        this.orcamentoService = orcamentoService;
        this.componenteService = componenteService;
    }

    @GetMapping
    public List<ItemOrcamentoResponse> listar(
            @RequestParam(required = false) Integer orcamentoId,
            @RequestParam(required = false) Integer componenteId,
            @RequestParam(required = false) Integer quantidadeMinima,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo
    ) {
        if (orcamentoId != null && componenteId != null) {
            return toResponseList(itemOrcamentoService.buscarPorOrcamentoEComponente(orcamentoId, componenteId));
        }

        if (orcamentoId != null) {
            return toResponseList(itemOrcamentoService.buscarPorOrcamento(orcamentoId));
        }

        if (componenteId != null) {
            return toResponseList(itemOrcamentoService.buscarPorComponente(componenteId));
        }

        if (quantidadeMinima != null) {
            return toResponseList(itemOrcamentoService.buscarPorQuantidadeMinima(quantidadeMinima));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(itemOrcamentoService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        return toResponseList(itemOrcamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemOrcamentoResponse> buscarPorId(@PathVariable Integer id) {
        return itemOrcamentoService.buscarPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ItemOrcamentoResponse> criar(@Valid @RequestBody ItemOrcamentoCadastroRequest request) {
        Optional<OrcamentoModel> orcamento = orcamentoService.buscarPorId(request.orcamentoId());
        if (orcamento.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<ComponenteModel> componente = componenteService.buscarPorId(request.componenteId());
        if (componente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ItemOrcamentoModel itemOrcamento = new ItemOrcamentoModel();
        itemOrcamento.setOrcamento(orcamento.get());
        itemOrcamento.setComponente(componente.get());
        itemOrcamento.setQuantidade(request.quantidade());
        itemOrcamento.setPreco(componente.get().getPreco());

        ItemOrcamentoModel salvo = itemOrcamentoService.salvar(itemOrcamento);
        recalcularPrecoOrcamento(request.orcamentoId());

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemOrcamentoResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ItemOrcamentoCadastroRequest request
    ) {
        Optional<ItemOrcamentoModel> itemAtual = itemOrcamentoService.buscarPorId(id);
        if (itemAtual.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<OrcamentoModel> orcamento = orcamentoService.buscarPorId(request.orcamentoId());
        if (orcamento.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<ComponenteModel> componente = componenteService.buscarPorId(request.componenteId());
        if (componente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Integer orcamentoAnteriorId = itemAtual.get().getOrcamento().getId();

        ItemOrcamentoModel itemOrcamento = itemAtual.get();
        itemOrcamento.setOrcamento(orcamento.get());
        itemOrcamento.setComponente(componente.get());
        itemOrcamento.setQuantidade(request.quantidade());
        itemOrcamento.setPreco(componente.get().getPreco());

        ItemOrcamentoModel atualizado = itemOrcamentoService.salvar(itemOrcamento);
        recalcularPrecoOrcamento(orcamentoAnteriorId);
        recalcularPrecoOrcamento(request.orcamentoId());

        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        Optional<ItemOrcamentoModel> itemOrcamento = itemOrcamentoService.buscarPorId(id);
        if (itemOrcamento.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Integer orcamentoId = itemOrcamento.get().getOrcamento().getId();
        if (itemOrcamentoService.buscarPorOrcamento(orcamentoId).size() <= 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (!itemOrcamentoService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        recalcularPrecoOrcamento(orcamentoId);
        return ResponseEntity.noContent().build();
    }

    private List<ItemOrcamentoResponse> toResponseList(List<ItemOrcamentoModel> itens) {
        return itens.stream()
                .map(this::toResponse)
                .toList();
    }

    private ItemOrcamentoResponse toResponse(ItemOrcamentoModel item) {
        return new ItemOrcamentoResponse(
                item.getId(),
                item.getOrcamento().getId(),
                item.getComponente().getId(),
                item.getQuantidade(),
                item.getPreco(),
                calcularSubtotal(item)
        );
    }

    private void recalcularPrecoOrcamento(Integer orcamentoId) {
        Optional<OrcamentoModel> orcamento = orcamentoService.buscarPorId(orcamentoId);
        if (orcamento.isEmpty()) {
            return;
        }

        List<ItemOrcamentoModel> itens = itemOrcamentoService.buscarPorOrcamento(orcamentoId);
        if (itens.isEmpty()) {
            return;
        }

        OrcamentoModel atualizado = orcamento.get();
        atualizado.setPreco(calcularTotal(itens));
        orcamentoService.salvar(atualizado);
    }

    private BigDecimal calcularTotal(List<ItemOrcamentoModel> itens) {
        return itens.stream()
                .map(this::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularSubtotal(ItemOrcamentoModel item) {
        return item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
    }
}

