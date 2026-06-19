package com.omni.besthardware.service;

import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.UsuarioModel;
import com.omni.besthardware.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UsuarioService extends AbstractCrudService<UsuarioModel, Integer> {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        super(usuarioRepository, "Usuario", UsuarioModel::setId);
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioModel> buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarPorEmailObrigatorio(String email) {
        return buscarPorEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado para email " + email + "."));
    }

    @Transactional(readOnly = true)
    public List<UsuarioModel> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmailIgnoreCase(email);
    }
}
