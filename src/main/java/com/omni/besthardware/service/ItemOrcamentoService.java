package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.ItemOrcamentoCadastroRequest;
import com.omni.besthardware.rest.dto.request.ItemOrcamentoFiltroRequest;
import com.omni.besthardware.rest.dto.response.ItemOrcamentoResponse;
import com.omni.besthardware.exception.ConflitoException;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.model.ItemOrcamentoModel;
import com.omni.besthardware.model.OrcamentoModel;
import com.omni.besthardware.repository.ItemOrcamentoRepository;
import com.omni.besthardware.repository.OrcamentoRepository;
import com.omni.besthardware.specifications.ItemOrcamentoSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico responsavel pelas regras de negocio de ItemOrcamento no projeto BestHardware.
 */
@Service
public class ItemOrcamentoService {

    private final ItemOrcamentoRepository itemOrcamentoRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final ComponenteService componenteService;
    private final OfertaPrecoService ofertaPrecoService;

    public ItemOrcamentoService(
            ItemOrcamentoRepository itemOrcamentoRepository,
            OrcamentoRepository orcamentoRepository,
            ComponenteService componenteService,
            OfertaPrecoService ofertaPrecoService
    ) {
        this.itemOrcamentoRepository = itemOrcamentoRepository;
        this.orcamentoRepository = orcamentoRepository;
        this.componenteService = componenteService;
        this.ofertaPrecoService = ofertaPrecoService;
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> listarTodos() {
        return itemOrcamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ItemOrcamentoModel> buscarPorId(Integer id) {
        return itemOrcamentoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarComFiltros(ItemOrcamentoFiltroRequest filtro) {
        return itemOrcamentoRepository.findAll(ItemOrcamentoSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoResponse> listarRespostas(ItemOrcamentoFiltroRequest filtro) {
        return buscarComFiltros(filtro).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemOrcamentoResponse buscarRespostaPorId(Integer id) {
        return buscarPorId(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item de orcamento", id));
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorOrcamento(Integer orcamentoId) {
        return itemOrcamentoRepository.findByOrcamentoId(orcamentoId);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorComponente(Integer componenteId) {
        return itemOrcamentoRepository.findByComponenteId(componenteId);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorOrcamentoEComponente(Integer orcamentoId, Integer componenteId) {
        return itemOrcamentoRepository.findByOrcamentoIdAndComponenteId(orcamentoId, componenteId);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorQuantidadeMinima(Integer quantidade) {
        return itemOrcamentoRepository.findByQuantidadeGreaterThanEqual(quantidade);
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return itemOrcamentoRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional
    public ItemOrcamentoModel salvar(ItemOrcamentoModel itemOrcamento) {
        return itemOrcamentoRepository.save(itemOrcamento);
    }

    @Transactional
    public ItemOrcamentoResponse criar(ItemOrcamentoCadastroRequest request) {
        OrcamentoModel orcamento = buscarOrcamentoObrigatorio(request.orcamentoId());
        ComponenteModel componente = componenteService.buscarPorId(request.componenteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", request.componenteId()));

        ItemOrcamentoModel itemOrcamento = new ItemOrcamentoModel();
        itemOrcamento.setOrcamento(orcamento);
        itemOrcamento.setComponente(componente);
        itemOrcamento.setQuantidade(request.quantidade());
        itemOrcamento.setPreco(ofertaPrecoService.calcularPrecoPreferencial(componente));

        ItemOrcamentoModel salvo = itemOrcamentoRepository.save(itemOrcamento);
        recalcularPrecoOrcamento(request.orcamentoId());
        return toResponse(salvo);
    }

    @Transactional
    public ItemOrcamentoResponse atualizar(Integer id, ItemOrcamentoCadastroRequest request) {
        ItemOrcamentoModel itemOrcamento = buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item de orcamento", id));
        OrcamentoModel orcamento = buscarOrcamentoObrigatorio(request.orcamentoId());
        ComponenteModel componente = componenteService.buscarPorId(request.componenteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", request.componenteId()));

        Integer orcamentoAnteriorId = itemOrcamento.getOrcamento().getId();

        itemOrcamento.setOrcamento(orcamento);
        itemOrcamento.setComponente(componente);
        itemOrcamento.setQuantidade(request.quantidade());
        itemOrcamento.setPreco(ofertaPrecoService.calcularPrecoPreferencial(componente));

        ItemOrcamentoModel atualizado = itemOrcamentoRepository.save(itemOrcamento);
        recalcularPrecoOrcamento(orcamentoAnteriorId);
        recalcularPrecoOrcamento(request.orcamentoId());
        return toResponse(atualizado);
    }

    @Transactional
    public Optional<ItemOrcamentoModel> atualizar(Integer id, ItemOrcamentoModel itemOrcamento) {
        if (!itemOrcamentoRepository.existsById(id)) {
            return Optional.empty();
        }

        itemOrcamento.setId(id);
        return Optional.of(itemOrcamentoRepository.save(itemOrcamento));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!itemOrcamentoRepository.existsById(id)) {
            return false;
        }

        itemOrcamentoRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        ItemOrcamentoModel itemOrcamento = buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item de orcamento", id));

        Integer orcamentoId = itemOrcamento.getOrcamento().getId();
        if (buscarPorOrcamento(orcamentoId).size() <= 1) {
            throw new ConflitoException("Nao e possivel remover o unico item do orcamento.");
        }

        itemOrcamentoRepository.deleteById(id);
        recalcularPrecoOrcamento(orcamentoId);
    }

    private OrcamentoModel buscarOrcamentoObrigatorio(Integer orcamentoId) {
        return orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento", orcamentoId));
    }

    private void recalcularPrecoOrcamento(Integer orcamentoId) {
        Optional<OrcamentoModel> orcamento = orcamentoRepository.findById(orcamentoId);
        if (orcamento.isEmpty()) {
            return;
        }

        List<ItemOrcamentoModel> itens = itemOrcamentoRepository.findByOrcamentoId(orcamentoId);
        if (itens.isEmpty()) {
            return;
        }

        OrcamentoModel atualizado = orcamento.get();
        atualizado.setPreco(calcularTotal(itens));
        orcamentoRepository.save(atualizado);
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

    private BigDecimal calcularTotal(List<ItemOrcamentoModel> itens) {
        return itens.stream()
                .map(this::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularSubtotal(ItemOrcamentoModel item) {
        return item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
    }
}
