package com.omni.besthardware.services;

import com.omni.besthardware.models.PerfilModel;
import com.omni.besthardware.repositories.PerfilRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
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
    public Optional<PerfilModel> buscarPorNomeExato(String nome) {
        return perfilRepository.findByNomeIgnoreCase(nome);
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
    public Optional<PerfilModel> atualizar(Integer id, PerfilModel perfil) {
        if (!perfilRepository.existsById(id)) {
            return Optional.empty();
        }

        perfil.setId(id);
        return Optional.of(perfilRepository.save(perfil));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!perfilRepository.existsById(id)) {
            return false;
        }

        perfilRepository.deleteById(id);
        return true;
    }
}
