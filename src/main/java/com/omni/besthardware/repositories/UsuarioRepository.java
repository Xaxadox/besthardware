package com.omni.besthardware.repositories;

import com.omni.besthardware.models.UsuarioModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    Optional<UsuarioModel> findByEmailIgnoreCase(String email);

    List<UsuarioModel> findByNomeContainingIgnoreCase(String nome);

    boolean existsByEmailIgnoreCase(String email);
}
