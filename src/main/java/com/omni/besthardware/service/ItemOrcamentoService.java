package com.omni.besthardware.service;

import com.omni.besthardware.exception.ConflitoException;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.model.ItemOrcamentoModel;
import com.omni.besthardware.model.OrcamentoModel;
import com.omni.besthardware.repository.ItemOrcamentoRepository;
import com.omni.besthardware.repository.OrcamentoRepository;
import com.omni.besthardware.rest.dto.request.ItemOrcamentoCadastroRequest;
import com.omni.besthardware.rest.dto.request.ItemOrcamentoFiltroRequest;
import com.omni.besthardware.rest.dto.response.ItemOrcamentoResponse;
import com.omni.besthardware.specifications.ItemOrcamentoSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ItemOrcamentoService extends AbstractCrudService<ItemOrcamentoModel, Integer> {

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
        super(itemOrcamentoRepository, "Item de orcamento", ItemOrcamentoModel::setId);
        this.itemOrcamentoRepository = itemOrcamentoRepository;
        this.orcamentoRepository = orcamentoRepository;
        this.componenteService = componenteService;
        this.ofertaPrecoService = ofertaPrecoService;
    }

    // -------------------------------------------------------------------------
    // Buscas
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<ItemOrcamentoModel> buscarComFiltros(ItemOrcamentoFiltroRequest filtro) {
        return itemOrcamentoRepository.findAll(ItemOrcamentoSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<ItemOrcamentoResponse> listarRespostas(ItemOrcamentoFiltroRequest filtro) {
        return buscarComFiltros(filtro).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ItemOrcamentoResponse buscarRespostaPorId(Integer id) {
        return toResponse(buscarObrigatorio(id));
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

    // -------------------------------------------------------------------------
    // Escrita
    // -------------------------------------------------------------------------

    @Transactional
    public ItemOrcamentoResponse criar(ItemOrcamentoCadastroRequest request) {
        OrcamentoModel orcamento = buscarOrcamentoObrigatorio(request.orcamentoId());
        ComponenteModel componente = componenteService.buscarObrigatorio(request.componenteId());

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
        ItemOrcamentoModel itemOrcamento = buscarObrigatorio(id);
        OrcamentoModel orcamento = buscarOrcamentoObrigatorio(request.orcamentoId());
        ComponenteModel componente = componenteService.buscarObrigatorio(request.componenteId());

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

    /**
     * Sobrescreve a exclusao padrao da superclasse para aplicar a regra de negocio:
     * nao e permitido remover o unico item de um orcamento.
     */
    @Override
    @Transactional
    public void excluirObrigatorio(Integer id) {
        ItemOrcamentoModel itemOrcamento = buscarObrigatorio(id);
        Integer orcamentoId = itemOrcamento.getOrcamento().getId();

        if (buscarPorOrcamento(orcamentoId).size() <= 1) {
            throw new ConflitoException("Nao e possivel remover o unico item do orcamento.");
        }

        itemOrcamentoRepository.deleteById(id);
        recalcularPrecoOrcamento(orcamentoId);
    }

    // -------------------------------------------------------------------------
    // Auxiliares
    // -------------------------------------------------------------------------

    private OrcamentoModel buscarOrcamentoObrigatorio(Integer orcamentoId) {
        return orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orcamento", orcamentoId));
    }

    private void recalcularPrecoOrcamento(Integer orcamentoId) {
        Optional<OrcamentoModel> orcamento = orcamentoRepository.findById(orcamentoId);
        if (orcamento.isEmpty()) return;

        List<ItemOrcamentoModel> itens = itemOrcamentoRepository.findByOrcamentoId(orcamentoId);
        if (itens.isEmpty()) return;

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
        return itens.stream().map(this::calcularSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularSubtotal(ItemOrcamentoModel item) {
        return item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
    }
}
