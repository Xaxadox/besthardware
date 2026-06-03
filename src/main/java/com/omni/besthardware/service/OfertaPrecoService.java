package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.OfertaPrecoFiltroRequest;
import com.omni.besthardware.rest.dto.request.OfertaPrecoRequest;
import com.omni.besthardware.rest.dto.response.OfertaPrecoResponse;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.model.OfertaPrecoModel;
import com.omni.besthardware.repository.OfertaPrecoRepository;
import com.omni.besthardware.specifications.OfertaPrecoSpecification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico responsavel pelas regras de negocio de OfertaPreco no projeto BestHardware.
 */
@Service
public class OfertaPrecoService {

    private final OfertaPrecoRepository ofertaPrecoRepository;
    private final ComponenteService componenteService;

    public OfertaPrecoService(OfertaPrecoRepository ofertaPrecoRepository, ComponenteService componenteService) {
        this.ofertaPrecoRepository = ofertaPrecoRepository;
        this.componenteService = componenteService;
    }

    @Transactional(readOnly = true)
    public List<OfertaPrecoModel> listarTodos() {
        return ofertaPrecoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OfertaPrecoModel> buscarPorId(Integer id) {
        return ofertaPrecoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public OfertaPrecoResponse buscarRespostaPorId(Integer id) {
        return buscarPorId(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oferta de preco", id));
    }

    @Transactional(readOnly = true)
    public List<OfertaPrecoModel> buscarComFiltros(OfertaPrecoFiltroRequest filtro) {
        return ofertaPrecoRepository.findAll(OfertaPrecoSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<OfertaPrecoResponse> listarRespostas(OfertaPrecoFiltroRequest filtro) {
        return buscarComFiltros(filtro).stream()
                .map(this::toResponse)
                .toList();
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
        validarComponenteExistente(componenteId);
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
        ComponenteModel componente = componenteService.buscarPorId(componenteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", componenteId));
        return calcularPrecoPreferencial(componente);
    }

    @Transactional
    public OfertaPrecoResponse criar(OfertaPrecoRequest request) {
        return toResponse(ofertaPrecoRepository.save(toModel(new OfertaPrecoModel(), request)));
    }

    @Transactional
    public OfertaPrecoResponse atualizar(Integer id, OfertaPrecoRequest request) {
        OfertaPrecoModel ofertaPreco = buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Oferta de preco", id));

        return toResponse(ofertaPrecoRepository.save(toModel(ofertaPreco, request)));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!ofertaPrecoRepository.existsById(id)) {
            return false;
        }

        ofertaPrecoRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Oferta de preco", id);
        }
    }

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
        ComponenteModel componente = componenteService.buscarPorId(request.componenteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", request.componenteId()));

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

    private void validarComponenteExistente(Integer componenteId) {
        if (componenteService.buscarPorId(componenteId).isEmpty()) {
            throw new RecursoNaoEncontradoException("Componente", componenteId);
        }
    }
}
