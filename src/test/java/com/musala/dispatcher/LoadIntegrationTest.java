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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void testLoadGet(Drone drone, Medication medication) throws Exception {
        medicationRepository.save(medication);
        droneRepository.save(drone);

        drone = droneRepository.findById(drone.getSerialNumber()).get();
        drone.getLoads().add(new Load()
                .setDrone(drone).setMedication(medication));
        droneRepository.save(drone);

        mvc.perform(get("/drones/" + drone.getSerialNumber() + "/medications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(medication.getName()))
                .andExpect(jsonPath("$[0].weight").value(medication.getWeight()))
                .andExpect(jsonPath("$[0].code").value(medication.getCode()))
                .andExpect(jsonPath("$[0].image").value(medication.getImage()))
                .andExpect(jsonPath("$[0].count").value(1));

        mvc.perform(get("/drones/" + drone.getSerialNumber()
                + "/medications/" + medication.getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name").value(medication.getName()))
                .andExpect(jsonPath("weight").value(medication.getWeight()))
                .andExpect(jsonPath("code").value(medication.getCode()))
                .andExpect(jsonPath("image").value(medication.getImage()))
                .andExpect(jsonPath("count").value(1));;
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.LoadProvider#provideLoads")
    public void testLoadPost(Drone drone, Medication medication) throws Exception {
        medicationRepository.save(medication);
        droneRepository.save(drone);

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

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.LoadProvider#provideLoads")
    public void testLoadDelete(Drone drone, Medication medication) throws Exception {
        medicationRepository.save(medication);
        droneRepository.save(drone);

        drone = droneRepository.findById(drone.getSerialNumber()).get();
        drone.getLoads().add(new Load()
                .setDrone(drone).setMedication(medication));
        droneRepository.save(drone);

        mvc.perform(delete("/drones/" + drone.getSerialNumber()
                        + "/medications/" + medication.getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertEquals(1, droneRepository.count());
        assertEquals(1, medicationRepository.count());
        assertEquals(0, droneRepository
                .findById(drone.getSerialNumber()).get().getLoads().size());
    }

    @Test
    public void testDependenciesDelete() throws Exception {
        Drone drone = DroneProvider.provideDrones().findFirst().get();
        Medication medication = MedicationProvider.provideMedications().findFirst().get();

        assertEquals(0, medicationRepository.count());
        medicationRepository.save(medication);
        assertEquals(1, medicationRepository.count());

        assertEquals(0, droneRepository.count());
        droneRepository.save(drone);
        assertEquals(1, droneRepository.count());

        drone = droneRepository.findById(drone.getSerialNumber()).get();
        drone.getLoads().add(new Load()
                .setDrone(drone).setMedication(medication));
        droneRepository.save(drone);

        assertEquals(1, droneRepository.count());
        droneRepository.deleteAll();
        assertEquals(0, droneRepository.count());

        assertEquals(1, medicationRepository.count());
        medicationRepository.deleteAll();
        assertEquals(0, medicationRepository.count());
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
