package com.musala.dispatcher.service;

import com.musala.dispatcher.data.CreateDroneRequest;
import com.musala.dispatcher.data.DroneResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface DroneService {

    @NotNull
    List<DroneResponse> findAll();

    @NotNull
    DroneResponse findById(@NotNull String droneId);

    @NotNull
    DroneResponse create(@NotNull CreateDroneRequest request);

    @NotNull
    DroneResponse update(@NotNull String droneId, @NotNull CreateDroneRequest request);

    void delete(@NotNull String droneId);
}
