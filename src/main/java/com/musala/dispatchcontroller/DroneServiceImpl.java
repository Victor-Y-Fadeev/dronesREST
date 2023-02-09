package com.musala.dispatchcontroller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {
    private static DroneRepository droneRepository;

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public List<DroneResponse> findAll() {
        return droneRepository.findAll().stream()
                .map(this::buildDroneResponse)
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public DroneResponse findById(@NotNull String droneSerialNumber) {
        return droneRepository.findById(droneSerialNumber)
                .map(this::buildDroneResponse)
                .orElseThrow(() -> new EntityNotFoundException("Drone " + droneSerialNumber + " is not found"));
    }

    @NotNull
    @Override
    @Transactional
    public DroneResponse createDrone(@NotNull CreateDroneRequest request) {
        return null;
    }

    @NotNull
    @Override
    @Transactional
    public DroneResponse update(@NotNull String droneSerialNumber, @NotNull CreateDroneRequest request) {
        return null;
    }

    @NotNull
    @Override
    @Transactional
    public void delete(@NotNull String droneSerialNumber) {

    }

    @NotNull
    private DroneResponse buildDroneResponse(@NotNull Drone drone) {
        return new DroneResponse()
                .setSerialNumber(drone.getSerialNumber())
                .setModel(drone.getModel())
                .setWeightLimit(drone.getWeightLimit())
                .setBatteryCapacity(drone.getBatteryCapacity())
                .setState(drone.getState())
                .setLoadedMedication(drone.getLoadedMedication().stream().map(
                        medication -> new MedicationResponse()
                                .setName(medication.getName())
                                .setWeight(medication.getWeight())
                                .setCode(medication.getCode())
                                .setImage(medication.getImage())
                        ).collect(Collectors.toSet()));
    }


}
