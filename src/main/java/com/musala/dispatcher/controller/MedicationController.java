package com.musala.dispatcher.controller;

import com.musala.dispatcher.data.CreateMedicationRequest;
import com.musala.dispatcher.data.MedicationResponse;
import com.musala.dispatcher.service.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/medications")
@AllArgsConstructor
public class MedicationController {
    private MedicationService medicationService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<MedicationResponse> findAll() {
        return medicationService.findAll();
    }

    @GetMapping(value = "/{medicationId}", produces = APPLICATION_JSON_VALUE)
    public MedicationResponse findById(@PathVariable String medicationId) {
        return medicationService.findById(medicationId);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public MedicationResponse create(@RequestBody CreateMedicationRequest request) {
        return medicationService.create(request);
    }

    @PatchMapping(value = "/{medicationId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public MedicationResponse update(@PathVariable String medicationId, @RequestBody CreateMedicationRequest request) {
        return medicationService.update(medicationId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{medicationId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String medicationId) {
        medicationService.delete(medicationId);
    }
}
