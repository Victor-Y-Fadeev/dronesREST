package com.musala.dispatcher.service;

import com.musala.dispatcher.data.CreateMedicationRequest;
import com.musala.dispatcher.data.MedicationResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface MedicationService {

    @NotNull
    List<MedicationResponse> findAll();

    @NotNull
    MedicationResponse findById(@NotNull String medicationId);

    void create(@NotNull CreateMedicationRequest request);

    void update(@NotNull String medicationId, @NotNull CreateMedicationRequest request);

    void delete(@NotNull String medicationId);
}
