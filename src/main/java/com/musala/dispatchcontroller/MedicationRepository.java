package com.musala.dispatchcontroller;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, String> { }
