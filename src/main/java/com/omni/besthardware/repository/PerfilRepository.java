package com.omni.besthardware.repository;

import com.omni.besthardware.model.PerfilModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio responsavel pelo acesso aos dados de Perfil.
 */
public interface PerfilRepository extends JpaRepository<PerfilModel, Integer> {
    Optional<PerfilModel> findByNomeIgnoreCase(String nome);

    List<PerfilModel> findByNomeContainingIgnoreCase(String nome);

    List<PerfilModel> findByComponentesId(Integer componenteId);
}
