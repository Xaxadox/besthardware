package com.omni.besthardware.repositories;

import com.omni.besthardware.models.gpuModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface gpuRepository extends JpaRepository<gpuModel, Integer> {
}