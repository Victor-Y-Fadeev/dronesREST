package com.musala.dispatcher.service;

import com.musala.dispatcher.data.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface LoadService {

    @NotNull
    List<LoadResponse> findAll(@NotNull String droneId);

    @NotNull
    LoadResponse findById(@NotNull String droneId, @NotNull String medicationId);

    void create(@NotNull String droneId, @NotNull CreateLoadRequest request);

    void update(@NotNull String droneId,
                        @NotNull String medicationId,
                        @NotNull CreateLoadRequest request);

    void delete(@NotNull String droneId, @NotNull String medicationId);
}
