package com.musala.dispatcher.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class FullResponse extends DroneResponse {
    private Set<LoadResponse> loads;
}
