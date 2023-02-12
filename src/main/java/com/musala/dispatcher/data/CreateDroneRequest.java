package com.musala.dispatcher.data;

import com.musala.dispatcher.entity.Model;
import com.musala.dispatcher.entity.State;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateDroneRequest {
    private String serialNumber;
    private Model model;
    private Integer weightLimit;
    private Integer batteryCapacity;
    private State state;
}
