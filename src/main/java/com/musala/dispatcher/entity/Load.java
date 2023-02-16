package com.musala.dispatcher.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "loads")
@IdClass(CompositeKey.class)
public class Load {

    @Id
    @ManyToOne
    @JoinColumn(name = "drone_serial_number")
    private Drone drone;

    @Id
    @ManyToOne
    @JoinColumn(name = "medication_code")
    private Medication medication;

    @Min(1)
    @ColumnDefault("1")
    @Column(nullable = false)
    private Integer count;

    @PrePersist
    @PreUpdate
    public void fillDefault() {
        if (count == null) {
            count = 1;
        }
    }

    @PreRemove
    public void removeDependencies() {
        drone.getLoads().remove(this);
        medication.getLoads().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Load)) {
            return false;
        }

        return drone.equals(((Load) o).drone)
                && medication.equals(((Load) o).medication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drone, medication);
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
