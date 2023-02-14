package com.musala.dispatcher;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Model;
import com.musala.dispatcher.entity.State;
import com.musala.dispatcher.repository.DroneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    public void testDroneDefaultsGet() throws Exception {
        repository.save(new Drone()
                .setSerialNumber("0")
                .setModel(Model.Lightweight)
                .setWeightLimit(0));

        mvc.perform(get("/drones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].serialNumber").value("0"))
                .andExpect(jsonPath("$[0].model").value("Lightweight"))
                .andExpect(jsonPath("$[0].weightLimit").value("0"))
                .andExpect(jsonPath("$[0].batteryCapacity").value("0"))
                .andExpect(jsonPath("$[0].state").value("IDLE"));
    }


    @Test
    public void testDroneDefaultsPost() throws Exception {
        mvc.perform(post("/drones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .writeValueAsString(new Drone()
                                        .setSerialNumber("0")
                                        .setModel(Model.Lightweight)
                                        .setWeightLimit(0))))
                .andExpect(status().isOk());

        Drone drone = repository.findById("0").get();
        assertEquals("0", drone.getSerialNumber());
        assertEquals(Model.Lightweight, drone.getModel());
        assertEquals(0, drone.getWeightLimit());
        assertEquals(0, drone.getBatteryCapacity());
        assertEquals(State.IDLE, drone.getState());
    }

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }
}
