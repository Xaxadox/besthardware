package com.omni.besthardware.repositories;

import com.omni.besthardware.models.usuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface usuarioRepository extends JpaRepository<usuarioModel, Integer> {
}