package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ItemOrcamentoRequest;
import com.omni.besthardware.dtos.ItemOrcamentoResponse;
import com.omni.besthardware.dtos.OrcamentoAtualizacaoRequest;
import com.omni.besthardware.dtos.OrcamentoRequest;
import com.omni.besthardware.dtos.OrcamentoResponse;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.models.ComponenteModel;
import com.omni.besthardware.models.ItemOrcamentoModel;
import com.omni.besthardware.models.OrcamentoModel;
import com.omni.besthardware.models.PerfilModel;
import com.omni.besthardware.models.UsuarioModel;
import com.omni.besthardware.services.ComponenteService;
import com.omni.besthardware.services.ItemOrcamentoService;
import com.omni.besthardware.services.OrcamentoService;
import com.omni.besthardware.services.PerfilService;
import com.omni.besthardware.services.UsuarioService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;
    private final ComponenteService componenteService;
    private final ItemOrcamentoService itemOrcamentoService;

    public OrcamentoController(
            OrcamentoService orcamentoService,
            UsuarioService usuarioService,
            PerfilService perfilService,
            ComponenteService componenteService,
            ItemOrcamentoService itemOrcamentoService
    ) {
        this.orcamentoService = orcamentoService;
        this.usuarioService = usuarioService;
        this.perfilService = perfilService;
        this.componenteService = componenteService;
        this.itemOrcamentoService = itemOrcamentoService;
    }

    @GetMapping
    public List<OrcamentoResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) Integer perfilId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo
    ) {
        if (nome != null) {
            return toResponseList(orcamentoService.buscarPorNome(nome));
        }

        if (usuarioId != null) {
            return toResponseList(orcamentoService.buscarPorUsuario(usuarioId));
        }

        if (perfilId != null) {
            return toResponseList(orcamentoService.buscarPorPerfil(perfilId));
        }

        if (dataInicial != null && dataFinal != null) {
            return toResponseList(orcamentoService.buscarPorPeriodoCriacao(dataInicial, dataFinal));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(orcamentoService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        return toResponseList(orcamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> buscarPorId(@PathVariable Integer id) {
        return orcamentoService.buscarPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento", id));
    }

    @PostMapping
    public ResponseEntity<OrcamentoResponse> criar(@Valid @RequestBody OrcamentoRequest request) {
        UsuarioModel usuario = usuarioService.buscarPorId(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
        PerfilModel perfil = perfilService.buscarPorId(request.perfilId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil", request.perfilId()));

        OrcamentoModel orcamento = new OrcamentoModel();
        orcamento.setNome(request.nome());
        orcamento.setDataCriacao(LocalDateTime.now());
        orcamento.setUsuario(usuario);
        orcamento.setPerfil(perfil);

        List<ItemOrcamentoModel> itens = new ArrayList<>();
        for (ItemOrcamentoRequest itemRequest : request.itens()) {
            ComponenteModel componente = componenteService.buscarPorId(itemRequest.componenteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", itemRequest.componenteId()));

            ItemOrcamentoModel item = new ItemOrcamentoModel();
            item.setOrcamento(orcamento);
            item.setComponente(componente);
            item.setQuantidade(itemRequest.quantidade());
            item.setPreco(componente.getPreco());
            itens.add(item);
        }

        orcamento.getItens().addAll(itens);
        orcamento.setPreco(calcularTotal(itens));

        OrcamentoModel salvo = orcamentoService.salvar(orcamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrcamentoResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody OrcamentoAtualizacaoRequest request
    ) {
        OrcamentoModel atualizado = orcamentoService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento", id));
        UsuarioModel usuario = usuarioService.buscarPorId(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
        PerfilModel perfil = perfilService.buscarPorId(request.perfilId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil", request.perfilId()));

        atualizado.setNome(request.nome());
        atualizado.setUsuario(usuario);
        atualizado.setPerfil(perfil);

        return ResponseEntity.ok(toResponse(orcamentoService.salvar(atualizado)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!orcamentoService.excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Orcamento", id);
        }

        return ResponseEntity.noContent().build();
    }

    private List<OrcamentoResponse> toResponseList(List<OrcamentoModel> orcamentos) {
        return orcamentos.stream()
                .map(this::toResponse)
                .toList();
    }

    private OrcamentoResponse toResponse(OrcamentoModel orcamento) {
        List<ItemOrcamentoResponse> itens = itemOrcamentoService.buscarPorOrcamento(orcamento.getId()).stream()
                .map(this::toItemResponse)
                .toList();

        return new OrcamentoResponse(
                orcamento.getId(),
                orcamento.getNome(),
                orcamento.getDataCriacao(),
                orcamento.getPreco(),
                orcamento.getUsuario().getId(),
                orcamento.getPerfil().getId(),
                itens
        );
    }

    private ItemOrcamentoResponse toItemResponse(ItemOrcamentoModel item) {
        return new ItemOrcamentoResponse(
                item.getId(),
                item.getOrcamento().getId(),
                item.getComponente().getId(),
                item.getQuantidade(),
                item.getPreco(),
                calcularSubtotal(item)
        );
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

