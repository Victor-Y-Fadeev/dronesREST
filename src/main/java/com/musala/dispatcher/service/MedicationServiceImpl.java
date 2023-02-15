package com.musala.dispatcher.service;

import com.musala.dispatcher.data.CreateMedicationRequest;
import com.musala.dispatcher.data.MedicationResponse;
import com.musala.dispatcher.entity.Medication;
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
public class MedicationServiceImpl implements MedicationService {
    private MedicationRepository medicationRepository;

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public List<MedicationResponse> findAll() {
        return medicationRepository.findAll().stream()
                .map(this::buildMedicationResponse)
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public MedicationResponse findById(@NotNull String medicationId) {
        return medicationRepository.findById(medicationId)
                .map(this::buildMedicationResponse)
                .orElseThrow(() -> new EntityNotFoundException("Medication " + medicationId + " is not found"));
    }

    @Override
    @Transactional
    public void create(@NotNull CreateMedicationRequest request) {
        medicationRepository.save(buildMedicationRequest(request));
    }

    @Override
    @Transactional
    public void update(@NotNull String medicationId, @NotNull CreateMedicationRequest request) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new EntityNotFoundException("Medication " + medicationId + " is not found"));
        medicationUpdate(medication, request);
        medicationRepository.save(medication);
    }

    @Override
    @Transactional
    public void delete(@NotNull String medicationId) {
        medicationRepository.deleteById(medicationId);
    }

    @NotNull
    private MedicationResponse buildMedicationResponse(@NotNull Medication medication) {
        return new MedicationResponse()
                .setName(medication.getName())
                .setWeight(medication.getWeight())
                .setCode(medication.getCode())
                .setImage(medication.getImage());
    }

    @NotNull
    private Medication buildMedicationRequest(@NotNull CreateMedicationRequest request) {
        return new Medication()
                .setName(request.getName())
                .setWeight(request.getWeight())
                .setCode(request.getCode())
                .setImage(request.getImage());
    }

    private void medicationUpdate(@NotNull Medication medication, @NotNull CreateMedicationRequest request) {
        ofNullable(request.getName()).map(medication::setName);
        ofNullable(request.getWeight()).map(medication::setWeight);
        ofNullable(request.getCode()).map(medication::setCode);
        ofNullable(request.getImage()).map(medication::setImage);
    }
}
