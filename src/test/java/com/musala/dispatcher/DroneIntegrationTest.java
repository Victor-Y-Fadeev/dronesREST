package com.musala.dispatcher;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.State;
import com.musala.dispatcher.repository.DroneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class DroneIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DroneRepository repository;

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDefaultDrones")
    public void testDefaultDroneGet(Drone expected) throws Exception {
        repository.save(expected);

        mvc.perform(get("/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].serialNumber").value(expected.getSerialNumber()))
                .andExpect(jsonPath("$[0].model").value(expected.getModel().name()))
                .andExpect(jsonPath("$[0].weightLimit").value(expected.getWeightLimit()))
                .andExpect(jsonPath("$[0].batteryCapacity").value(0))
                .andExpect(jsonPath("$[0].state").value(State.IDLE.name()));

        mvc.perform(get("/drones/" + expected.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("serialNumber").value(expected.getSerialNumber()))
                .andExpect(jsonPath("model").value(expected.getModel().name()))
                .andExpect(jsonPath("weightLimit").value(expected.getWeightLimit()))
                .andExpect(jsonPath("batteryCapacity").value(0))
                .andExpect(jsonPath("state").value(State.IDLE.name()));
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDefaultDrones")
    public void testDefaultDronePost(Drone expected) throws Exception {
        mvc.perform(post("/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .writeValueAsString(expected)))
                .andExpect(status().isCreated());

        Drone actual = repository.findById(expected.getSerialNumber()).get();
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getWeightLimit(), actual.getWeightLimit());
        assertEquals(0, actual.getBatteryCapacity());
        assertEquals(State.IDLE, actual.getState());
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDrones")
    public void testDroneGet(Drone expected) throws Exception {
        repository.save(expected);

        mvc.perform(get("/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].serialNumber").value(expected.getSerialNumber()))
                .andExpect(jsonPath("$[0].model").value(expected.getModel().name()))
                .andExpect(jsonPath("$[0].weightLimit").value(expected.getWeightLimit()))
                .andExpect(jsonPath("$[0].batteryCapacity").value(expected.getBatteryCapacity()))
                .andExpect(jsonPath("$[0].state").value(expected.getState().name()));

        mvc.perform(get("/drones/" + expected.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("serialNumber").value(expected.getSerialNumber()))
                .andExpect(jsonPath("model").value(expected.getModel().name()))
                .andExpect(jsonPath("weightLimit").value(expected.getWeightLimit()))
                .andExpect(jsonPath("batteryCapacity").value(expected.getBatteryCapacity()))
                .andExpect(jsonPath("state").value(expected.getState().name()));
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDrones")
    public void testDronePost(Drone expected) throws Exception {
        mvc.perform(post("/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .writeValueAsString(expected)))
                .andExpect(status().isCreated());

        Drone actual = repository.findById(expected.getSerialNumber()).get();
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getWeightLimit(), actual.getWeightLimit());
        assertEquals(expected.getBatteryCapacity(), actual.getBatteryCapacity());
        assertEquals(expected.getState(), actual.getState());
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDrones")
    public void testDroneDelete(Drone expected) throws Exception {
        repository.save(expected);

        mvc.perform(delete("/drones/" + expected.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertTrue(repository.findAll().isEmpty());
    }

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }
}
