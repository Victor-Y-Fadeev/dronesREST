package com.musala.dispatcher.service;

import com.musala.dispatcher.data.CreateLoadRequest;
import com.musala.dispatcher.data.LoadResponse;
import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Load;
import com.musala.dispatcher.repository.DroneRepository;
import com.musala.dispatcher.repository.MedicationRepository;
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
    private MedicationRepository medicationRepository;
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

    @Override
    @Transactional
    public void create(@NotNull String droneId, @NotNull CreateLoadRequest request) {
        Drone drone = findDroneById(droneId);
        Load load = buildLoadRequest(drone, request);

        if (drone.getLoads().contains(load)) {
            throw new IllegalArgumentException("Medication " + request.getCode() + " is already there");
        }

        drone.getLoads().add(load);
        droneRepository.save(drone);
    }

    @Override
    @Transactional
    public void update(@NotNull String droneId,
                               @NotNull String medicationId,
                               @NotNull CreateLoadRequest request) {
        Drone drone = findDroneById(droneId);
        Load load = findLoadById(drone, medicationId);

        loadUpdate(load, request);
        droneRepository.save(drone);
    }

    @Override
    @Transactional
    public void delete(@NotNull String droneId, @NotNull String medicationId) {
        Drone drone = findDroneById(droneId);
        Load load = findLoadById(drone, medicationId);

        drone.getLoads().remove(load);
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
        Load load = new Load().setDrone(drone)
                .setCount(request.getCount())
                .setMedication(medicationRepository.findById(request.getCode())
                        .orElseThrow(() ->
                                new EntityNotFoundException("Medication " + request.getCode() + " is not found")));

        loadValidate(load);
        return load;
    }

    private void loadUpdate(@NotNull Load load, @NotNull CreateLoadRequest request) {
        ofNullable(request.getCount()).map(load::setCount);
        loadValidate(load);
    }

    private void loadValidate(@NotNull Load load) {
        if (load.getDrone().getLoads().stream()
                .filter(droneLoad -> !droneLoad.getMedication().equals(load.getMedication()))
                .mapToInt(droneLoad ->
                        droneLoad.getMedication().getWeight() * ofNullable(droneLoad.getCount()).orElse(1)).sum()
                + load.getMedication().getWeight() * ofNullable(load.getCount()).orElse(1)
                > load.getDrone().getWeightLimit()) {
            throw new IllegalArgumentException("Drone " + load.getDrone().getSerialNumber() + " is overloaded");
        }
    }
}
