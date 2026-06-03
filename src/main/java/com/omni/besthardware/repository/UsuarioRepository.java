package com.omni.besthardware.repository;

import com.omni.besthardware.model.UsuarioModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio responsavel pelo acesso aos dados de Usuario.
 */
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    Optional<UsuarioModel> findByEmailIgnoreCase(String email);

    List<UsuarioModel> findByNomeContainingIgnoreCase(String nome);

    boolean existsByEmailIgnoreCase(String email);
}
