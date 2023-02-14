package com.musala.dispatcher.controller;

import com.musala.dispatcher.data.CreateDroneRequest;
import com.musala.dispatcher.data.DroneResponse;
import com.musala.dispatcher.service.DroneService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/drones")
@AllArgsConstructor
public class DroneController {
    private DroneService droneService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<DroneResponse> findAll(DroneSpec droneSpec) {
        return droneService.findAll(droneSpec);
    }

    @GetMapping(value = "/{droneId}", produces = APPLICATION_JSON_VALUE)
    public DroneResponse findById(@PathVariable String droneId) {
        return droneService.findById(droneId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void create(@RequestBody CreateDroneRequest request) {
        droneService.create(request);
    }

    @PatchMapping(value = "/{droneId}", consumes = APPLICATION_JSON_VALUE)
    public void update(@PathVariable String droneId, @RequestBody CreateDroneRequest request) {
        droneService.update(droneId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{droneId}")
    public void delete(@PathVariable String droneId) {
        droneService.delete(droneId);
    }
}
