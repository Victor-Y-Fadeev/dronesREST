package com.musala.dispatcher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Range;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @NotBlank
    @Column(length = 100, updatable = false, nullable = false, unique = true)
    private String serialNumber;

    @Column(length = 1, updatable = false, nullable = false)
    private Model model;

    @Range(min = 0, max = 500)
    @Column(updatable = false, nullable = false)
    private Integer weightLimit;

    @ColumnDefault("0")
    @Range(min = 0, max = 100)
    @Column(nullable = false)
    private Integer batteryCapacity;

    @ColumnDefault("0")
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private State state;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "load",
            joinColumns = @JoinColumn(name = "droneSerialNumber"),
            inverseJoinColumns = @JoinColumn(name = "medicationCode"))
    private Set<Medication> loadedMedication;

    @PrePersist
    public void fillDefault() {
        if (batteryCapacity == null) {
            batteryCapacity = 0;
        }

        if (state == null) {
            state = State.IDLE;
        }
    }
}
