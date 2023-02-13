package com.musala.dispatcher.entity;

import java.io.Serializable;

public class CompositeKey implements Serializable {
    private Drone drone;
    private Medication medication;

    public CompositeKey() { }

    public CompositeKey(Drone drone, Medication medication) {
        this.drone = drone;
        this.medication = medication;
    }
}
