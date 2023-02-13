package com.musala.dispatcher.repository;

import com.musala.dispatcher.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepository extends JpaRepository<Drone, String> {
}
