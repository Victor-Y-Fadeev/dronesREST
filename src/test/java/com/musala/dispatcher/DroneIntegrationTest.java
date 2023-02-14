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

import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
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
    public void testDefaultDroneGet(Drone drone) throws Exception {
        repository.save(drone);

        mvc.perform(get("/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].serialNumber").value(drone.getSerialNumber()))
                .andExpect(jsonPath("$[0].model").value(drone.getModel().name()))
                .andExpect(jsonPath("$[0].weightLimit").value(drone.getWeightLimit()))
                .andExpect(jsonPath("$[0].batteryCapacity").value(0))
                .andExpect(jsonPath("$[0].state").value(State.IDLE.name()));

        mvc.perform(get("/drones/" + drone.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("serialNumber").value(drone.getSerialNumber()))
                .andExpect(jsonPath("model").value(drone.getModel().name()))
                .andExpect(jsonPath("weightLimit").value(drone.getWeightLimit()))
                .andExpect(jsonPath("batteryCapacity").value(0))
                .andExpect(jsonPath("state").value(State.IDLE.name()));
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDefaultDrones")
    public void testDefaultDronePost(Drone drone) throws Exception {
        mvc.perform(post("/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .writeValueAsString(drone)))
                .andExpect(status().isCreated());

        Drone found = repository.findById(drone.getSerialNumber()).get();
        assertEquals(drone.getSerialNumber(), found.getSerialNumber());
        assertEquals(drone.getModel(), found.getModel());
        assertEquals(drone.getWeightLimit(), found.getWeightLimit());
        assertEquals(0, found.getBatteryCapacity());
        assertEquals(State.IDLE, found.getState());
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDrones")
    public void testDroneGet(Drone drone) throws Exception {
        repository.save(drone);

        mvc.perform(get("/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].serialNumber").value(drone.getSerialNumber()))
                .andExpect(jsonPath("$[0].model").value(drone.getModel().name()))
                .andExpect(jsonPath("$[0].weightLimit").value(drone.getWeightLimit()))
                .andExpect(jsonPath("$[0].batteryCapacity").value(drone.getBatteryCapacity()))
                .andExpect(jsonPath("$[0].state").value(drone.getState().name()));

        mvc.perform(get("/drones/" + drone.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("serialNumber").value(drone.getSerialNumber()))
                .andExpect(jsonPath("model").value(drone.getModel().name()))
                .andExpect(jsonPath("weightLimit").value(drone.getWeightLimit()))
                .andExpect(jsonPath("batteryCapacity").value(drone.getBatteryCapacity()))
                .andExpect(jsonPath("state").value(drone.getState().name()));
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDrones")
    public void testDronePost(Drone drone) throws Exception {
        mvc.perform(post("/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .writeValueAsString(drone)))
                .andExpect(status().isCreated());

        Drone found = repository.findById(drone.getSerialNumber()).get();
        assertEquals(drone.getSerialNumber(), found.getSerialNumber());
        assertEquals(drone.getModel(), found.getModel());
        assertEquals(drone.getWeightLimit(), found.getWeightLimit());
        assertEquals(drone.getBatteryCapacity(), found.getBatteryCapacity());
        assertEquals(drone.getState(), found.getState());
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideDrones")
    public void testDroneDelete(Drone drone) throws Exception {
        repository.save(drone);

        mvc.perform(delete("/drones/" + drone.getSerialNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertTrue(repository.findAll().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideFilteringDronesByState")
    public void testDroneFilteringByState(Collection<Drone> list,
                                          State state) throws Exception {
        list.forEach(repository::save);

        mvc.perform(get("/drones?state=" + state.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize((int) list.stream()
                        .filter(drone -> drone.getState().equals(state))
                        .count())));
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideFilteringDronesByBatteryCapacity")
    public void testDroneFilteringByState(Collection<Drone> list,
                                          Integer batteryCapacity) throws Exception {
        list.forEach(repository::save);

        mvc.perform(get("/drones?battery=" + batteryCapacity)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize((int) list.stream()
                        .filter(drone -> drone.getBatteryCapacity() >= batteryCapacity)
                        .count())));
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.DroneProvider#provideFilteringDronesByStateAndBatteryCapacity")
    public void testDroneFilteringByStateAndBatteryCapacity(Collection<Drone> list,
                                                            State state, Integer batteryCapacity) throws Exception {
        list.forEach(repository::save);

        mvc.perform(get("/drones?state=" + state.name() + "&battery=" + batteryCapacity)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize((int) list.stream()
                        .filter(drone -> drone.getState().equals(state))
                        .filter(drone -> drone.getBatteryCapacity() >= batteryCapacity)
                        .count())));

        mvc.perform(get("/drones?battery=" + batteryCapacity + "&state=" + state.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize((int) list.stream()
                        .filter(drone -> drone.getBatteryCapacity() >= batteryCapacity)
                        .filter(drone -> drone.getState().equals(state))
                        .count())));
    }

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }
}
