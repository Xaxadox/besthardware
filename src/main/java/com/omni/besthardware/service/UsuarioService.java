package com.omni.besthardware.service;

import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.UsuarioModel;
import com.omni.besthardware.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico responsavel pelas regras de negocio de Usuario no projeto BestHardware.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioModel> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarObrigatorio(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario", id));
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

    @Transactional
    public UsuarioModel salvar(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Optional<UsuarioModel> atualizar(Integer id, UsuarioModel usuario) {
        if (!usuarioRepository.existsById(id)) {
            return Optional.empty();
        }

        usuario.setId(id);
        return Optional.of(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioModel atualizarObrigatorio(Integer id, UsuarioModel usuario) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuario", id);
        }

        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }

        usuarioRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Usuario", id);
        }
    }
}
