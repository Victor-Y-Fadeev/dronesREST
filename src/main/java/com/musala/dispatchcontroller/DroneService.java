package com.musala.dispatchcontroller;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface DroneService {

    @NotNull
    List<DroneResponse> findAll();

    @NotNull
    DroneResponse findById(@NotNull String droneId);

    @NotNull
    DroneResponse createDrone(@NotNull CreateDroneRequest request);

    @NotNull
    DroneResponse update(@NotNull String droneId, @NotNull CreateDroneRequest request);

    void delete(@NotNull String droneId);
}
