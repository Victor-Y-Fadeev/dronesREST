package com.musala.dispatcher.service;

import com.musala.dispatcher.controller.DroneSpec;
import com.musala.dispatcher.data.CreateDroneRequest;
import com.musala.dispatcher.data.DroneResponse;
import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.repository.DroneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
public class DroneServiceImpl implements DroneService {
    private DroneRepository droneRepository;

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public List<DroneResponse> findAll(DroneSpec droneSpec) {
        return droneRepository.findAll(droneSpec).stream()
                .map(this::buildDroneResponse)
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public DroneResponse findById(@NotNull String droneId) {
        return droneRepository.findById(droneId)
                .map(this::buildDroneResponse)
                .orElseThrow(() -> new EntityNotFoundException("Drone " + droneId + " is not found"));
    }

    @Override
    @Transactional
    public void create(@NotNull CreateDroneRequest request) {
        droneRepository.save(buildDroneRequest(request));
    }

    @Override
    @Transactional
    public void update(@NotNull String droneId, @NotNull CreateDroneRequest request) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone " + droneId + " is not found"));
        droneUpdate(drone, request);
        droneRepository.save(drone);
    }

    @Override
    @Transactional
    public void delete(@NotNull String droneId) {
        droneRepository.deleteById(droneId);
    }

    @NotNull
    private DroneResponse buildDroneResponse(@NotNull Drone drone) {
        return new DroneResponse()
                .setSerialNumber(drone.getSerialNumber())
                .setModel(drone.getModel())
                .setWeightLimit(drone.getWeightLimit())
                .setBatteryCapacity(drone.getBatteryCapacity())
                .setState(drone.getState());
    }

    @NotNull
    private Drone buildDroneRequest(@NotNull CreateDroneRequest request) {
        return new Drone()
                .setSerialNumber(request.getSerialNumber())
                .setModel(request.getModel())
                .setWeightLimit(request.getWeightLimit())
                .setBatteryCapacity(request.getBatteryCapacity())
                .setState(request.getState());
    }

    private void droneUpdate(@NotNull Drone drone, @NotNull CreateDroneRequest request) {
        ofNullable(request.getSerialNumber()).map(drone::setSerialNumber);
        ofNullable(request.getModel()).map(drone::setModel);
        ofNullable(request.getWeightLimit()).map(drone::setWeightLimit);
        ofNullable(request.getBatteryCapacity()).map(drone::setBatteryCapacity);
        ofNullable(request.getState()).map(drone::setState);
    }
}
