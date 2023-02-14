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

    @NotNull
    MedicationResponse create(@NotNull CreateMedicationRequest request);

    @NotNull
    MedicationResponse update(@NotNull String medicationId, @NotNull CreateMedicationRequest request);

    void delete(@NotNull String medicationId);
}