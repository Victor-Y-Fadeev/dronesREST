package com.musala.dispatcher.service;

import com.musala.dispatcher.data.CreateLoadRequest;
import com.musala.dispatcher.data.LoadResponse;
import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Load;
import com.musala.dispatcher.entity.Medication;
import com.musala.dispatcher.repository.DroneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
public class LoadServiceImpl implements LoadService {
    private DroneRepository droneRepository;

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public List<LoadResponse> findAll(@NotNull String droneId) {
        return findDroneById(droneId).getLoads().stream()
                .map(this::buildLoadResponse)
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public LoadResponse findById(@NotNull String droneId, @NotNull String medicationId) {
        return buildLoadResponse(findLoadById(findDroneById(droneId), medicationId));
    }

    @NotNull
    @Override
    @Transactional
    public LoadResponse create(@NotNull String droneId, @NotNull CreateLoadRequest request) {
        Drone drone = findDroneById(droneId);
        Load load = buildLoadRequest(drone, request);

        drone.getLoads().add(load);
        droneRepository.save(drone);
        return buildLoadResponse(load);
    }

    @NotNull
    @Override
    @Transactional
    public LoadResponse update(@NotNull String droneId,
                               @NotNull String medicationId,
                               @NotNull CreateLoadRequest request) {
        Drone drone = findDroneById(droneId);
        Load load = findLoadById(drone, medicationId);
        loadUpdate(load, request);

        drone.getLoads().add(load);
        droneRepository.save(drone);
        return buildLoadResponse(load);
    }

    @NotNull
    @Override
    @Transactional
    public void delete(@NotNull String droneId, @NotNull String medicationId) {
        Drone drone = findDroneById(droneId);
        drone.getLoads().removeIf(load -> load.getMedication().getCode().equals(medicationId));
        droneRepository.save(drone);
    }

    @NotNull
    private Drone findDroneById(@NotNull String droneId) {
        return droneRepository.findById(droneId)
                .orElseThrow(() -> new EntityNotFoundException("Drone " + droneId + " is not found"));
    }

    @NotNull
    private Load findLoadById(@NotNull Drone drone, @NotNull String medicationId) {
        return drone.getLoads().stream()
                .filter(load -> load.getMedication().getCode().equals(medicationId))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Medication " + medicationId + " is not found"));
    }

    @NotNull
    private LoadResponse buildLoadResponse(@NotNull Load load) {
        LoadResponse loadResponse = new LoadResponse().setCount(load.getCount());
        loadResponse.setName(load.getMedication().getName())
                .setWeight(load.getMedication().getWeight())
                .setCode(load.getMedication().getCode())
                .setImage(load.getMedication().getImage());
        return loadResponse;
    }

    @NotNull
    private Load buildLoadRequest(@NotNull Drone drone, @NotNull CreateLoadRequest request) {
        return drone.getLoads().stream()
                .filter(droneLoad -> droneLoad.getMedication().getCode().equals(request.getCode()))
                .map(load -> load
                        .setCount(load.getCount() + request.getCount()))
                .findAny().orElse(new Load()
                        .setCount(request.getCount()))
                .setDrone(drone)
                .setMedication(new Medication()
                        .setName(request.getName())
                        .setWeight(request.getWeight())
                        .setCode(request.getCode())
                        .setImage(request.getImage()));
    }

    private void loadUpdate(@NotNull Load load, @NotNull CreateLoadRequest request) {
        ofNullable(request.getCount()).map(load::setCount);

        Medication medication = load.getMedication();
        ofNullable(request.getName()).map(medication::setName);
        ofNullable(request.getWeight()).map(medication::setWeight);
        ofNullable(request.getCode()).map(medication::setCode);
        ofNullable(request.getImage()).map(medication::setImage);
    }
}
