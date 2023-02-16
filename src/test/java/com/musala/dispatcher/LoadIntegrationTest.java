package com.musala.dispatcher;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.dispatcher.data.CreateLoadRequest;
import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Load;
import com.musala.dispatcher.entity.Medication;
import com.musala.dispatcher.repository.DroneRepository;
import com.musala.dispatcher.repository.MedicationRepository;
import org.hibernate.Hibernate;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class LoadIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.LoadProvider#provideLoads")
    public void testLoadPost(Drone drone, Medication medication) throws Exception {
        droneRepository.save(drone);
        medicationRepository.save(medication);


        mvc.perform(post("/drones/" + drone.getSerialNumber() + "/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadToRequest(medication)))
                .andExpect(status().isCreated());

        Load load = droneRepository.findById(drone.getSerialNumber()).get()
                .getLoads().stream().findFirst().get();
        assertEquals(drone, load.getDrone());
        assertEquals(medication, load.getMedication());
        assertEquals(1, load.getCount());
    }

    @AfterEach
    public void cleanUp() {
        droneRepository.deleteAll();
        medicationRepository.deleteAll();
    }

    private static String loadToRequest(Medication medication) throws JsonProcessingException {
        return loadToRequest(medication, null);
    }

    private static String loadToRequest(Medication medication, Integer count) throws JsonProcessingException {
        CreateLoadRequest request = new CreateLoadRequest()
                .setCount(count);

        request.setName(medication.getName())
                .setWeight(medication.getWeight())
                .setCode(medication.getCode())
                .setImage(medication.getImage());

        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writeValueAsString(request);
    }
}
