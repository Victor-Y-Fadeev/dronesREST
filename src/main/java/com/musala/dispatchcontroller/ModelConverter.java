package com.musala.dispatchcontroller;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ModelConverter implements AttributeConverter<Drone.Model, String> {

    @Override
    public String convertToDatabaseColumn(Drone.Model model) {
        if (model == null) {
            new IllegalArgumentException();
        }

        return model.getCode();
    }

    @Override
    public Drone.Model convertToEntityAttribute(String code) {
        if (code == null) {
            new IllegalArgumentException();
        }

        return Stream.of(Drone.Model.values())
                .filter(model -> model.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
