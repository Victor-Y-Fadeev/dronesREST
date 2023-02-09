package com.musala.dispatchcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/drones")
@RequiredArgsConstructor
public class DroneController {
    private DroneService droneService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<DroneResponse> findAll() {
        return droneService.findAll();
    }

    @GetMapping(value = "/{droneSerialNumber}", produces = APPLICATION_JSON_VALUE)
    public DroneResponse findById(@PathVariable String droneSerialNumber) {
        return droneService.findById(droneSerialNumber);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public DroneResponse create(@RequestBody CreateDroneRequest request) {
        return droneService.createUser(request);
    }

    @PatchMapping(value = "/{droneSerialNumber}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public DroneResponse update(@PathVariable String droneSerialNumber, @RequestBody CreateDroneRequest request) {
        return droneService.update(droneSerialNumber, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{droneSerialNumber}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String droneSerialNumber) {
        droneService.delete(droneSerialNumber);
    }
}
