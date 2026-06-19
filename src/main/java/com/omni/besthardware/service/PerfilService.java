package com.omni.besthardware.service;

import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.model.PerfilModel;
import com.omni.besthardware.repository.PerfilRepository;
import com.omni.besthardware.rest.dto.request.PerfilRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico responsavel pelas regras de negocio de Perfil no projeto BestHardware.
 *
 * <p>Estende AbstractCrudService para herdar buscarObrigatorio(), atualizarObrigatorio(),
 * excluirObrigatorio() e demais operacoes CRUD padrao, evitando reimplementar o mesmo
 * padrao orElseThrow em cada service de dominio.</p>
 */
@Service
public class PerfilService extends AbstractCrudService<PerfilModel, Integer> {

    private final PerfilRepository perfilRepository;
    private final ComponenteService componenteService;

    public PerfilService(PerfilRepository perfilRepository, ComponenteService componenteService) {
        super(perfilRepository, "Perfil", PerfilModel::setId);
        this.perfilRepository = perfilRepository;
        this.componenteService = componenteService;
    }

    @Transactional(readOnly = true)
    public Optional<PerfilModel> buscarPorNomeExato(String nome) {
        return perfilRepository.findByNomeIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public PerfilModel buscarPorNomeExatoObrigatorio(String nome) {
        return buscarPorNomeExato(nome)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil nao encontrado para nome " + nome + "."));
    }

    @Transactional(readOnly = true)
    public List<PerfilModel> buscarPorNome(String nome) {
        return perfilRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<PerfilModel> buscarPorComponente(Integer componenteId) {
        return perfilRepository.findByComponentesId(componenteId);
    }

    // -------------------------------------------------------------------------
    // Operacoes com semantica de PerfilRequest (componentes por ID)
    // -------------------------------------------------------------------------

    @Transactional
    public PerfilModel criar(PerfilRequest request) {
        return salvar(toModel(request));
    }

    /**
     * Atualiza um Perfil a partir de um PerfilRequest, resolvendo os componentes por ID.
     * Sobrecarga proposital — nao sobrescreve atualizarObrigatorio(ID, T) da superclasse,
     * que continua disponivel para chamadas que ja tenham o model montado.
     */
    @Transactional
    public PerfilModel atualizarObrigatorio(Integer id, PerfilRequest request) {
        buscarObrigatorio(id); // lanca 404 se nao existir
        PerfilModel perfil = toModel(request);
        perfil.setId(id);
        return salvar(perfil);
    }

    // -------------------------------------------------------------------------
    // Auxiliares
    // -------------------------------------------------------------------------

    private PerfilModel toModel(PerfilRequest request) {
        PerfilModel perfil = new PerfilModel();
        perfil.setNome(request.nome());
        if (request.componenteIds() != null) {
            for (Integer componenteId : request.componenteIds()) {
                ComponenteModel componente = componenteService.buscarObrigatorio(componenteId);
                perfil.getComponentes().add(componente);
            }
        }
        return perfil;
    }
}
