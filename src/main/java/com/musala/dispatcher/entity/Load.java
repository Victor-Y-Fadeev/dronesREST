package com.musala.dispatcher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

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
    public void fillDefault() {
        if (count == null) {
            count = 1;
        }
    }
}
