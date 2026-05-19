package com.omni.besthardware.repositories;

import com.omni.besthardware.models.monitorModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface monitorRepository extends JpaRepository<monitorModel, Integer> {
}