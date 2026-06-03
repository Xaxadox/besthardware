package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.ItemOrcamentoRequest;
import com.omni.besthardware.rest.dto.response.ItemOrcamentoResponse;
import com.omni.besthardware.rest.dto.request.OrcamentoAtualizacaoRequest;
import com.omni.besthardware.rest.dto.request.OrcamentoFiltroRequest;
import com.omni.besthardware.rest.dto.request.OrcamentoRequest;
import com.omni.besthardware.rest.dto.response.OrcamentoResponse;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.model.ItemOrcamentoModel;
import com.omni.besthardware.model.OrcamentoModel;
import com.omni.besthardware.model.PerfilModel;
import com.omni.besthardware.model.UsuarioModel;
import com.omni.besthardware.repository.ItemOrcamentoRepository;
import com.omni.besthardware.repository.OrcamentoRepository;
import com.omni.besthardware.specifications.OrcamentoSpecification;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico responsavel pelas regras de negocio de Orcamento no projeto BestHardware.
 */
@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ItemOrcamentoRepository itemOrcamentoRepository;
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;
    private final ComponenteService componenteService;
    private final OfertaPrecoService ofertaPrecoService;

    public OrcamentoService(
            OrcamentoRepository orcamentoRepository,
            ItemOrcamentoRepository itemOrcamentoRepository,
            UsuarioService usuarioService,
            PerfilService perfilService,
            ComponenteService componenteService,
            OfertaPrecoService ofertaPrecoService
    ) {
        this.orcamentoRepository = orcamentoRepository;
        this.itemOrcamentoRepository = itemOrcamentoRepository;
        this.usuarioService = usuarioService;
        this.perfilService = perfilService;
        this.componenteService = componenteService;
        this.ofertaPrecoService = ofertaPrecoService;
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> listarTodos() {
        return orcamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OrcamentoModel> buscarPorId(Integer id) {
        return orcamentoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarComFiltros(OrcamentoFiltroRequest filtro) {
        return orcamentoRepository.findAll(OrcamentoSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<OrcamentoResponse> listarRespostas(OrcamentoFiltroRequest filtro) {
        return buscarComFiltros(filtro).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrcamentoResponse buscarRespostaPorId(Integer id) {
        return buscarPorId(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento", id));
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorNome(String nome) {
        return orcamentoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorUsuario(Integer usuarioId) {
        return orcamentoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorPerfil(Integer perfilId) {
        return orcamentoRepository.findByPerfilId(perfilId);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorPeriodoCriacao(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return orcamentoRepository.findByDataCriacaoBetween(dataInicial, dataFinal);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return orcamentoRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional
    public OrcamentoModel salvar(OrcamentoModel orcamento) {
        return orcamentoRepository.save(orcamento);
    }

    @Transactional
    public OrcamentoResponse criar(OrcamentoRequest request) {
        UsuarioModel usuario = usuarioService.buscarPorId(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
        PerfilModel perfil = perfilService.buscarPorId(request.perfilId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil", request.perfilId()));

        OrcamentoModel orcamento = new OrcamentoModel();
        orcamento.setNome(request.nome());
        orcamento.setDataCriacao(LocalDateTime.now());
        orcamento.setUsuario(usuario);
        orcamento.setPerfil(perfil);

        List<ItemOrcamentoModel> itens = request.itens().stream()
                .map(itemRequest -> criarItem(orcamento, itemRequest))
                .toList();

        orcamento.getItens().addAll(itens);
        orcamento.setPreco(calcularTotal(itens));

        return toResponse(orcamentoRepository.save(orcamento));
    }

    @Transactional
    public OrcamentoResponse atualizar(Integer id, OrcamentoAtualizacaoRequest request) {
        OrcamentoModel orcamento = buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento", id));
        UsuarioModel usuario = usuarioService.buscarPorId(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", request.usuarioId()));
        PerfilModel perfil = perfilService.buscarPorId(request.perfilId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil", request.perfilId()));

        orcamento.setNome(request.nome());
        orcamento.setUsuario(usuario);
        orcamento.setPerfil(perfil);

        return toResponse(orcamentoRepository.save(orcamento));
    }

    @Transactional
    public Optional<OrcamentoModel> atualizar(Integer id, OrcamentoModel orcamento) {
        if (!orcamentoRepository.existsById(id)) {
            return Optional.empty();
        }

        orcamento.setId(id);
        return Optional.of(orcamentoRepository.save(orcamento));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!orcamentoRepository.existsById(id)) {
            return false;
        }

        orcamentoRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Orcamento", id);
        }
    }

    private ItemOrcamentoModel criarItem(OrcamentoModel orcamento, ItemOrcamentoRequest itemRequest) {
        ComponenteModel componente = componenteService.buscarPorId(itemRequest.componenteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", itemRequest.componenteId()));

        ItemOrcamentoModel item = new ItemOrcamentoModel();
        item.setOrcamento(orcamento);
        item.setComponente(componente);
        item.setQuantidade(itemRequest.quantidade());
        item.setPreco(ofertaPrecoService.calcularPrecoPreferencial(componente));
        return item;
    }

    private OrcamentoResponse toResponse(OrcamentoModel orcamento) {
        List<ItemOrcamentoModel> itens = orcamento.getId() == null
                ? orcamento.getItens()
                : itemOrcamentoRepository.findByOrcamentoId(orcamento.getId());

        return new OrcamentoResponse(
                orcamento.getId(),
                orcamento.getNome(),
                orcamento.getDataCriacao(),
                orcamento.getPreco(),
                orcamento.getUsuario().getId(),
                orcamento.getPerfil().getId(),
                itens.stream().map(this::toItemResponse).toList()
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
