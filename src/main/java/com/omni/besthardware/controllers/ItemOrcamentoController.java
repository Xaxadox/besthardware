package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ItemOrcamentoCadastroRequest;
import com.omni.besthardware.dtos.ItemOrcamentoFiltroRequest;
import com.omni.besthardware.dtos.ItemOrcamentoResponse;
import com.omni.besthardware.exceptions.ConflitoException;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<ItemOrcamentoResponse> listar(@ModelAttribute ItemOrcamentoFiltroRequest filtro) {
        return toResponseList(itemOrcamentoService.buscarComFiltros(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemOrcamentoResponse> buscarPorId(@PathVariable Integer id) {
        return itemOrcamentoService.buscarPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item de orcamento", id));
    }

    @PostMapping
    public ResponseEntity<ItemOrcamentoResponse> criar(@Valid @RequestBody ItemOrcamentoCadastroRequest request) {
        OrcamentoModel orcamento = orcamentoService.buscarPorId(request.orcamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento", request.orcamentoId()));
        ComponenteModel componente = componenteService.buscarPorId(request.componenteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", request.componenteId()));

        ItemOrcamentoModel itemOrcamento = new ItemOrcamentoModel();
        itemOrcamento.setOrcamento(orcamento);
        itemOrcamento.setComponente(componente);
        itemOrcamento.setQuantidade(request.quantidade());
        itemOrcamento.setPreco(componente.getPreco());

        ItemOrcamentoModel salvo = itemOrcamentoService.salvar(itemOrcamento);
        recalcularPrecoOrcamento(request.orcamentoId());

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemOrcamentoResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ItemOrcamentoCadastroRequest request
    ) {
        ItemOrcamentoModel itemOrcamento = itemOrcamentoService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item de orcamento", id));
        OrcamentoModel orcamento = orcamentoService.buscarPorId(request.orcamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento", request.orcamentoId()));
        ComponenteModel componente = componenteService.buscarPorId(request.componenteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", request.componenteId()));

        Integer orcamentoAnteriorId = itemOrcamento.getOrcamento().getId();

        itemOrcamento.setOrcamento(orcamento);
        itemOrcamento.setComponente(componente);
        itemOrcamento.setQuantidade(request.quantidade());
        itemOrcamento.setPreco(componente.getPreco());

        ItemOrcamentoModel atualizado = itemOrcamentoService.salvar(itemOrcamento);
        recalcularPrecoOrcamento(orcamentoAnteriorId);
        recalcularPrecoOrcamento(request.orcamentoId());

        return ResponseEntity.ok(toResponse(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        ItemOrcamentoModel itemOrcamento = itemOrcamentoService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item de orcamento", id));

        Integer orcamentoId = itemOrcamento.getOrcamento().getId();
        if (itemOrcamentoService.buscarPorOrcamento(orcamentoId).size() <= 1) {
            throw new ConflitoException("Nao e possivel remover o unico item do orcamento.");
        }

        if (!itemOrcamentoService.excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Item de orcamento", id);
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

