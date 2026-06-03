package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.ComponenteFiltroRequest;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.repository.ComponenteRepository;
import com.omni.besthardware.specifications.ComponenteSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico responsavel pelas regras de negocio de Componente no projeto BestHardware.
 */
@Service
public class ComponenteService {

    private final ComponenteRepository componenteRepository;

    public ComponenteService(ComponenteRepository componenteRepository) {
        this.componenteRepository = componenteRepository;
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> listarTodos() {
        return componenteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ComponenteModel> buscarPorId(Integer id) {
        return componenteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarComFiltros(ComponenteFiltroRequest filtro) {
        return componenteRepository.findAll(ComponenteSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public ComponenteModel buscarObrigatorio(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Componente", id));
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarPorTipo(String tipo) {
        return componenteRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return componenteRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarPorPerfil(Integer perfilId) {
        return componenteRepository.findByPerfisId(perfilId);
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarCompativeisCom(Integer componenteCompativelId) {
        return componenteRepository.findByComponentesCompativeisId(componenteCompativelId);
    }

    @Transactional
    public ComponenteModel salvar(ComponenteModel componente) {
        return componenteRepository.save(componente);
    }

    @Transactional
    public Optional<ComponenteModel> atualizar(Integer id, ComponenteModel componente) {
        if (!componenteRepository.existsById(id)) {
            return Optional.empty();
        }

        componente.setId(id);
        return Optional.of(componenteRepository.save(componente));
    }

    @Transactional
    public ComponenteModel atualizarObrigatorio(Integer id, ComponenteModel componente) {
        if (!componenteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Componente", id);
        }

        componente.setId(id);
        return componenteRepository.save(componente);
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!componenteRepository.existsById(id)) {
            return false;
        }

        componenteRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Componente", id);
        }
    }
}
