package com.musala.dispatchcontroller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

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
    public DroneResponse findById(@NotNull String droneId) {
        return droneRepository.findById(droneId)
                .map(this::buildDroneResponse)
                .orElseThrow(() -> new EntityNotFoundException("Drone " + droneId + " is not found"));
    }

    @NotNull
    @Override
    @Transactional
    public DroneResponse createDrone(@NotNull CreateDroneRequest request) {
        return buildDroneResponse(droneRepository.save(buildDroneRequest(request)));
    }

    @NotNull
    @Override
    @Transactional
    public DroneResponse update(@NotNull String droneId, @NotNull CreateDroneRequest request) {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone " + droneId + " is not found"));
        droneUpdate(drone, request);
        return buildDroneResponse(droneRepository.save(drone));
    }

    @NotNull
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
