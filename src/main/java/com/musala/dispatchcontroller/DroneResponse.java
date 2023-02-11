package com.musala.dispatchcontroller;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DroneResponse {
    private String serialNumber;
    private Model model;
    private Integer weightLimit;
    private Integer batteryCapacity;
    private State state;
}
