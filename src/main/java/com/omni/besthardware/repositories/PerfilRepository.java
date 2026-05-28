package com.omni.besthardware.repositories;

import com.omni.besthardware.models.PerfilModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<PerfilModel, Integer> {
    Optional<PerfilModel> findByNomeIgnoreCase(String nome);

    List<PerfilModel> findByNomeContainingIgnoreCase(String nome);

    List<PerfilModel> findByComponentesId(Integer componenteId);
}
