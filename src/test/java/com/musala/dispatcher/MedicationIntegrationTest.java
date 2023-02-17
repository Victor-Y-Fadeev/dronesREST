package com.musala.dispatcher;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.dispatcher.data.CreateMedicationRequest;
import com.musala.dispatcher.entity.Medication;
import com.musala.dispatcher.repository.MedicationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
    @MethodSource("com.musala.dispatcher.MedicationProvider#provideMedications")
    public void testMedicationPost(Medication medication) throws Exception {
        mvc.perform(post("/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicationToRequest(medication)))
                .andExpect(status().isCreated());

        Medication found = repository.findById(medication.getCode()).get();
        assertEquals(medication.getName(), found.getName());
        assertEquals(medication.getWeight(), found.getWeight());
        assertEquals(medication.getCode(), found.getCode());
        assertEquals(medication.getImage(), found.getImage());
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.MedicationProvider#provideMedications")
    public void testMedicationPatch(Medication medication) throws Exception {
        Medication defaultMedication = MedicationProvider.provideMedications().findFirst().get();
        medication.setCode(defaultMedication.getCode());
        repository.save(defaultMedication);

        mvc.perform(patch("/medications/" + defaultMedication.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicationToRequest(medication)))
                .andExpect(status().isOk());


        Medication found = repository.findById(medication.getCode()).get();
        assertEquals(medication.getName(), found.getName());
        assertTrue(medication.getWeight().equals(defaultMedication.getWeight())
                || !medication.getWeight().equals(found.getWeight()));
        assertEquals(medication.getImage(), found.getImage());
    }

    @ParameterizedTest
    @MethodSource("com.musala.dispatcher.MedicationProvider#provideMedications")
    public void testMedicationDelete(Medication medication) throws Exception {
        repository.save(medication);

        mvc.perform(delete("/medications/" + medication.getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertTrue(repository.findAll().isEmpty());
    }

    @ParameterizedTest
    @MethodSource({
            "com.musala.dispatcher.MedicationProvider#provideWrongNameMedications",
            "com.musala.dispatcher.MedicationProvider#provideWrongWeightMedications",
            "com.musala.dispatcher.MedicationProvider#provideWrongCodeMedications"
    })
    public void testWrongMedicationPost(Medication medication) throws Exception {
        mvc.perform(post("/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicationToRequest(medication)))
                .andExpect(status().isBadRequest());

        assertEquals(0, repository.count());
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

    @Test
    public void testDuplicatePost() throws Exception {
        Medication medication = MedicationProvider.provideMedications().findFirst().get();
        repository.save(medication);

        mvc.perform(post("/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicationToRequest(medication)))
                .andExpect(status().isBadRequest());
    }

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }

    private static String medicationToRequest(Medication medication) throws JsonProcessingException {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writeValueAsString(new CreateMedicationRequest()
                        .setName(medication.getName())
                        .setWeight(medication.getWeight())
                        .setCode(medication.getCode())
                        .setImage(medication.getImage()));
    }
}
