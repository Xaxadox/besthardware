package com.omni.besthardware.repositories;

import com.omni.besthardware.models.ramModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ramRepository extends JpaRepository<ramModel, Integer> {
}