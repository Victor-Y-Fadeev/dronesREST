package com.musala.dispatcher.repository;

import com.musala.dispatcher.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, String> {
}
