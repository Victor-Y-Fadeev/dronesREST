package com.musala.dispatcher.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Range;

import java.util.Objects;
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

    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "drone",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Load> loads;

    @PrePersist
    @PreUpdate
    public void fillDefault() {
        if (batteryCapacity == null) {
            batteryCapacity = 0;
        }

        if (state == null) {
            state = State.IDLE;
        }
    }

    @PreRemove
    public void removeDependencies() {
        loads.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Drone)) {
            return false;
        }

        return serialNumber.equals(((Drone) o).serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
