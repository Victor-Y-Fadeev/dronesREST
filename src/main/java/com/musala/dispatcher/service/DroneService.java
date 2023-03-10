package com.musala.dispatcher.service;

import com.musala.dispatcher.controller.DroneSpec;
import com.musala.dispatcher.data.CreateDroneRequest;
import com.musala.dispatcher.data.DroneResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface DroneService {

    @NotNull
    List<DroneResponse> findAll(DroneSpec droneSpec);

    @NotNull
    DroneResponse findById(@NotNull String droneId);

    void create(@NotNull CreateDroneRequest request);

    void update(@NotNull String droneId, @NotNull CreateDroneRequest request);

    void delete(@NotNull String droneId);
}
