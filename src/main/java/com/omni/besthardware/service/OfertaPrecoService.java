package com.omni.besthardware.service;

import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.model.OfertaPrecoModel;
import com.omni.besthardware.repository.OfertaPrecoRepository;
import com.omni.besthardware.rest.dto.request.OfertaPrecoFiltroRequest;
import com.omni.besthardware.rest.dto.request.OfertaPrecoRequest;
import com.omni.besthardware.rest.dto.response.OfertaPrecoResponse;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.specifications.OfertaPrecoSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OfertaPrecoService extends AbstractCrudService<OfertaPrecoModel, Integer> {

    private final OfertaPrecoRepository ofertaPrecoRepository;
    private final ComponenteService componenteService;

    public OfertaPrecoService(OfertaPrecoRepository ofertaPrecoRepository, ComponenteService componenteService) {
        super(ofertaPrecoRepository, "Oferta de preco", OfertaPrecoModel::setId);
        this.ofertaPrecoRepository = ofertaPrecoRepository;
        this.componenteService = componenteService;
    }

    // -------------------------------------------------------------------------
    // Buscas
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<OfertaPrecoModel> buscarComFiltros(OfertaPrecoFiltroRequest filtro) {
        return ofertaPrecoRepository.findAll(OfertaPrecoSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<OfertaPrecoResponse> listarRespostas(OfertaPrecoFiltroRequest filtro) {
        return buscarComFiltros(filtro).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OfertaPrecoResponse buscarRespostaPorId(Integer id) {
        return toResponse(buscarObrigatorio(id));
    }

    @Transactional(readOnly = true)
    public List<OfertaPrecoModel> buscarPorComponente(Integer componenteId) {
        return ofertaPrecoRepository.findByComponenteId(componenteId);
    }

    @Transactional(readOnly = true)
    public Optional<OfertaPrecoModel> buscarMenorOferta(Integer componenteId) {
        return ofertaPrecoRepository.findFirstByComponenteIdOrderByPrecoAvistaAsc(componenteId);
    }

    @Transactional(readOnly = true)
    public OfertaPrecoResponse buscarMenorOfertaResposta(Integer componenteId) {
        // Valida existencia do componente via buscarObrigatorio (lanca 404 se ausente)
        componenteService.buscarObrigatorio(componenteId);
        return buscarMenorOferta(componenteId)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oferta de preco para componente", componenteId));
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularPrecoPreferencial(ComponenteModel componente) {
        if (componente == null || componente.getId() == null) {
            return BigDecimal.ZERO;
        }
        return buscarMenorOferta(componente.getId())
                .map(OfertaPrecoModel::getPrecoAvista)
                .orElse(componente.getPreco());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularPrecoPreferencial(Integer componenteId) {
        return calcularPrecoPreferencial(componenteService.buscarObrigatorio(componenteId));
    }

    // -------------------------------------------------------------------------
    // Escrita
    // -------------------------------------------------------------------------

    @Transactional
    public OfertaPrecoResponse criar(OfertaPrecoRequest request) {
        return toResponse(ofertaPrecoRepository.save(toModel(new OfertaPrecoModel(), request)));
    }

    @Transactional
    public OfertaPrecoResponse atualizar(Integer id, OfertaPrecoRequest request) {
        return toResponse(ofertaPrecoRepository.save(toModel(buscarObrigatorio(id), request)));
    }

    // -------------------------------------------------------------------------
    // Auxiliares
    // -------------------------------------------------------------------------

    public OfertaPrecoResponse toResponse(OfertaPrecoModel ofertaPreco) {
        ComponenteModel componente = ofertaPreco.getComponente();
        return new OfertaPrecoResponse(
                ofertaPreco.getId(),
                ofertaPreco.getLoja(),
                ofertaPreco.getPrecoAvista(),
                ofertaPreco.getPrecoParcelado(),
                ofertaPreco.getParcelas(),
                ofertaPreco.getCupom(),
                ofertaPreco.getUrlProduto(),
                ofertaPreco.getFonte(),
                ofertaPreco.getObservacoes(),
                ofertaPreco.getDataColeta(),
                componente.getId(),
                componente.getTipo()
        );
    }

    private OfertaPrecoModel toModel(OfertaPrecoModel ofertaPreco, OfertaPrecoRequest request) {
        ComponenteModel componente = componenteService.buscarObrigatorio(request.componenteId());
        ofertaPreco.setLoja(request.loja());
        ofertaPreco.setPrecoAvista(request.precoAvista());
        ofertaPreco.setPrecoParcelado(request.precoParcelado());
        ofertaPreco.setParcelas(request.parcelas());
        ofertaPreco.setCupom(request.cupom());
        ofertaPreco.setUrlProduto(request.urlProduto());
        ofertaPreco.setFonte(request.fonte());
        ofertaPreco.setObservacoes(request.observacoes());
        ofertaPreco.setDataColeta(request.dataColeta());
        ofertaPreco.setComponente(componente);
        return ofertaPreco;
    }
}
