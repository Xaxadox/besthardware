package com.omni.besthardware.repositories;

import com.omni.besthardware.models.cpuModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface cpuRepository extends JpaRepository<cpuModel, Integer> {
}