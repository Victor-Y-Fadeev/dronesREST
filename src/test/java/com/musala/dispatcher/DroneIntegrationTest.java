package com.musala.dispatcher;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Model;
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

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @MethodSource("provideDefaultDrones")
    public void testDefaultDroneGet(Drone expected) throws Exception {
        repository.save(expected);

        mvc.perform(get("/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].serialNumber").value(expected.getSerialNumber()))
                .andExpect(jsonPath("$[0].model").value(expected.getModel().name()))
                .andExpect(jsonPath("$[0].weightLimit").value(expected.getWeightLimit()))
                .andExpect(jsonPath("$[0].batteryCapacity").value(0))
                .andExpect(jsonPath("$[0].state").value(State.IDLE.name()));
    }

    @ParameterizedTest
    @MethodSource("provideDefaultDrones")
    public void testDefaultDronePost(Drone expected) throws Exception {
        mvc.perform(post("/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .writeValueAsString(expected)))
                .andExpect(status().isOk());

        Drone actual = repository.findById(expected.getSerialNumber()).get();
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getModel(), actual.getModel());
        assertEquals(expected.getWeightLimit(), actual.getWeightLimit());
        assertEquals(0, actual.getBatteryCapacity());
        assertEquals(State.IDLE, actual.getState());
    }

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }

    private static Stream<Drone> provideDefaultDrones() {
        return Stream.of(new Drone())
                .flatMap(drone -> Arrays.stream(Model.values())
                        .map(model -> drone.setModel(model)))
                .flatMap(drone -> Stream.of(0, 100, 200, 300, 400, 500)
                        .map(weightLimit -> drone.setWeightLimit(weightLimit)))
                .peek(drone -> drone.setSerialNumber(
                        drone.getModel().toString() + drone.getWeightLimit().toString()));
    }
}
