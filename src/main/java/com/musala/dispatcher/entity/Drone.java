package com.musala.dispatcher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @Size(max = 100)
    @Column(name = "serialNumber", nullable = false, unique = true)
    private String serialNumber;

    private Model model;

    @Range(min = 0, max = 500)
    @Column(name = "weightLimit", nullable = false)
    private Integer weightLimit;

    @Range(min = 0, max = 100)
    @Column(name = "batteryCapacity", nullable = false)
    private Integer batteryCapacity;

    @Enumerated(EnumType.ORDINAL)
    private State state;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "load",
            joinColumns = @JoinColumn(name = "droneSerialNumber"),
            inverseJoinColumns = @JoinColumn(name = "medicationCode"))
    private Set<Medication> loadedMedication;
}
