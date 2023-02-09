package com.musala.dispatchcontroller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {
    private static DroneRepository droneRepository;

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public List<DroneResponse> findAll() {
        return null;
    }

    @NotNull
    @Override
    @Transactional(readOnly = true)
    public DroneResponse findById(@NotNull String droneSerialNumber) {
        return null;
    }

    @NotNull
    @Override
    @Transactional
    public DroneResponse createUser(@NotNull CreateDroneRequest request) {
        return null;
    }

    @NotNull
    @Override
    @Transactional
    public DroneResponse update(@NotNull String droneSerialNumber, @NotNull CreateDroneRequest request) {
        return null;
    }

    @NotNull
    @Override
    @Transactional
    public void delete(@NotNull String droneSerialNumber) {

    }
}
