package com.omni.besthardware.repositories;

import com.omni.besthardware.models.armazenamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface armazenamentoRepository extends JpaRepository<armazenamentoModel, Integer> {
}