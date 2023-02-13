package com.musala.dispatcher.data;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateLoadRequest extends CreateMedicationRequest {
    private String count;
}
