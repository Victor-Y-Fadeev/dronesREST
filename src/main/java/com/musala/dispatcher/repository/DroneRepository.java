package com.musala.dispatcher.repository;

import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DroneRepository extends PagingAndSortingRepository<Drone, String>,
        JpaSpecificationExecutor<Drone>, JpaRepository<Drone, String> {

    List<Drone> findByState(State state);

    List<Drone> findByBatteryCapacity(Integer batteryCapacity);

    List<Drone> findByStateAndBatteryCapacity(State state, Integer batteryCapacity);
}
