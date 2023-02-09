package com.musala.dispatchcontroller;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateMedicationRequest {
    private String name;
    private Integer weight;
    private String code;
    private String image;
}
