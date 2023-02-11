package com.musala.dispatchcontroller;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

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
