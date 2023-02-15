package com.musala.dispatcher.controller;

import com.musala.dispatcher.data.CreateLoadRequest;
import com.musala.dispatcher.data.LoadResponse;
import com.musala.dispatcher.service.LoadService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/drones/{droneId}/medications")
@AllArgsConstructor
public class LoadController {
    private LoadService loadService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<LoadResponse> findAll(@PathVariable String droneId) {
        return loadService.findAll(droneId);
    }

    @GetMapping(value = "/{medicationId}", produces = APPLICATION_JSON_VALUE)
    public LoadResponse findById(@PathVariable String droneId,
                                 @PathVariable String medicationId) {
        return loadService.findById(droneId, medicationId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void create(@PathVariable String droneId,
                               @RequestBody CreateLoadRequest request) {
        loadService.create(droneId, request);
    }

    @PatchMapping(value = "/{medicationId}", consumes = APPLICATION_JSON_VALUE)
    public void update(@PathVariable String droneId,
                               @PathVariable String medicationId,
                               @RequestBody CreateLoadRequest request) {
        loadService.update(droneId, medicationId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{medicationId}")
    public void delete(@PathVariable String droneId, @PathVariable String medicationId) {
        loadService.delete(droneId, medicationId);
    }
}
