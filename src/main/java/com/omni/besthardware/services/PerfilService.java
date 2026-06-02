package com.omni.besthardware.services;

import com.omni.besthardware.dtos.PerfilRequest;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.models.ComponenteModel;
import com.omni.besthardware.models.PerfilModel;
import com.omni.besthardware.repositories.PerfilRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final ComponenteService componenteService;

    public PerfilService(PerfilRepository perfilRepository, ComponenteService componenteService) {
        this.perfilRepository = perfilRepository;
        this.componenteService = componenteService;
    }

    @Transactional(readOnly = true)
    public List<PerfilModel> listarTodos() {
        return perfilRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PerfilModel> buscarPorId(Integer id) {
        return perfilRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public PerfilModel buscarObrigatorio(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil", id));
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

    @Transactional
    public PerfilModel salvar(PerfilModel perfil) {
        return perfilRepository.save(perfil);
    }

    @Transactional
    public PerfilModel criar(PerfilRequest request) {
        return perfilRepository.save(toModel(request));
    }

    @Transactional
    public Optional<PerfilModel> atualizar(Integer id, PerfilModel perfil) {
        if (!perfilRepository.existsById(id)) {
            return Optional.empty();
        }

        perfil.setId(id);
        return Optional.of(perfilRepository.save(perfil));
    }

    @Transactional
    public PerfilModel atualizarObrigatorio(Integer id, PerfilRequest request) {
        if (!perfilRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Perfil", id);
        }

        PerfilModel perfil = toModel(request);
        perfil.setId(id);
        return perfilRepository.save(perfil);
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!perfilRepository.existsById(id)) {
            return false;
        }

        perfilRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Perfil", id);
        }
    }

    private PerfilModel toModel(PerfilRequest request) {
        PerfilModel perfil = new PerfilModel();
        perfil.setNome(request.nome());

        if (request.componenteIds() == null) {
            return perfil;
        }

        for (Integer componenteId : request.componenteIds()) {
            ComponenteModel componente = componenteService.buscarObrigatorio(componenteId);
            perfil.getComponentes().add(componente);
        }

        return perfil;
    }
}
