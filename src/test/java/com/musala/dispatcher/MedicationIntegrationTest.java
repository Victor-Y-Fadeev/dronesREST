package com.musala.dispatcher;

import com.musala.dispatcher.entity.Drone;
import com.musala.dispatcher.entity.Medication;
import com.musala.dispatcher.repository.DroneRepository;
import com.musala.dispatcher.repository.MedicationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class MedicationIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MedicationRepository repository;

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.MedicationProvider#provideMedications")
    public void testMedicationGet(Medication medication) throws Exception {
        repository.save(medication);

        mvc.perform(get("/medications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(medication.getName()))
                .andExpect(jsonPath("$[0].weight").value(medication.getWeight()))
                .andExpect(jsonPath("$[0].code").value(medication.getCode()))
                .andExpect(jsonPath("$[0].image").value(medication.getImage()));

        mvc.perform(get("/medications/" + medication.getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name").value(medication.getName()))
                .andExpect(jsonPath("weight").value(medication.getWeight()))
                .andExpect(jsonPath("code").value(medication.getCode()))
                .andExpect(jsonPath("image").value(medication.getImage()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "{", "}", "/", "\\"})
    public void testWrongPost(String content) throws Exception {
        mvc.perform(post("/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"0", "_", "A"})
    public void testNotFoundGet(String serialNumber) throws Exception {
        mvc.perform(get("/medications/" + serialNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }
}
