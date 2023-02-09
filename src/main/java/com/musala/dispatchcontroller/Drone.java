package com.musala.dispatchcontroller;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.util.stream.Stream;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public enum State {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING;
    }

    public enum Model {
        Lightweight("L"), Middleweight("M"), Cruiserweight("C"), Heavyweight("H");

        private String code;

        private Model(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    @Converter(autoApply = true)
    public class ModelConverter implements AttributeConverter<Model, String> {

        @Override
        public String convertToDatabaseColumn(Model model) {
            if (model == null) {
                new IllegalArgumentException();
            }

            return model.getCode();
        }

        @Override
        public Model convertToEntityAttribute(String code) {
            if (code == null) {
                new IllegalArgumentException();
            }

            return Stream.of(Model.values())
                    .filter(model -> model.getCode().equals(code))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
