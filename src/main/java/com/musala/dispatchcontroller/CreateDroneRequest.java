package com.musala.dispatchcontroller;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateDroneRequest {
    private String serialNumber;
    private Drone.Model model;
    private Integer weightLimit;
    private Integer batteryCapacity;
    private Drone.State state;
}
