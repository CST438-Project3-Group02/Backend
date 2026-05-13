package com.roomie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomie.dto.HouseholdDTO;
import com.roomie.entity.Household;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.service.HouseholdService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HouseholdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HouseholdService householdService;

    @Autowired
    private ObjectMapper objectMapper;

    private HouseholdDTO testDTO;
    private Household testHousehold;

    @BeforeEach
    void setUp() {
        testDTO = new HouseholdDTO(
            1L,
            "Test House",
            1200f,
            3,
            "123 Main St",
            "Carmel",
            "CA",
            "93923",
            "US"
        );
        testHousehold = new Household();
        testHousehold.setHouseholdName("Test House");
        testHousehold.setRentCost(1200f);
    }

    @Test
    void getAllHouseholds_returns200_withList() throws Exception {
        when(householdService.getAllHouseholds()).thenReturn(List.of(testDTO));

        mockMvc
            .perform(get("/api/households"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].householdName").value("Test House"))
            .andExpect(jsonPath("$[0].rentCost").value(1200.0));
    }

    @Test
    void getAllHouseholds_returns200_withEmptyList() throws Exception {
        when(householdService.getAllHouseholds()).thenReturn(List.of());

        mockMvc
            .perform(get("/api/households"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getHouseholdById_returns200_whenFound() throws Exception {
        when(householdService.getHouseholdById(1L)).thenReturn(testDTO);

        mockMvc
            .perform(get("/api/households/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.householdName").value("Test House"))
            .andExpect(jsonPath("$.city").value("Carmel"));
    }

    @Test
    void getHouseholdById_returns404_whenNotFound() throws Exception {
        when(householdService.getHouseholdById(99L)).thenThrow(
            new ResourceNotFoundException("Household", 99L)
        );

        mockMvc
            .perform(get("/api/households/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getHouseholdWithProfiles_returns200() throws Exception {
        when(householdService.getHouseholdWithProfiles(1L)).thenReturn(testDTO);

        mockMvc
            .perform(get("/api/households/1/profiles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.householdName").value("Test House"));
    }

    @Test
    void getHouseholdWithProfiles_returns404_whenNotFound() throws Exception {
        when(householdService.getHouseholdWithProfiles(99L)).thenThrow(
            new ResourceNotFoundException("Household", 99L)
        );

        mockMvc
            .perform(get("/api/households/99/profiles"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getHouseholdWithChores_returns200() throws Exception {
        when(householdService.getHouseholdWithChores(1L)).thenReturn(testDTO);

        mockMvc
            .perform(get("/api/households/1/chores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.householdName").value("Test House"));
    }

    @Test
    void getHouseholdWithExpenses_returns200() throws Exception {
        when(householdService.getHouseholdWithExpenses(1L)).thenReturn(testDTO);

        mockMvc
            .perform(get("/api/households/1/expenses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.householdName").value("Test House"));
    }

    @Test
    void createHousehold_returns201_withDTO() throws Exception {
        when(
            householdService.createHousehold(any(Household.class), eq(1L))
        ).thenReturn(testHousehold);
        when(householdService.toDTO(any(Household.class))).thenReturn(testDTO);

        Map<String, Object> payload = Map.of(
            "profileId",
            1,
            "householdName",
            "Test House",
            "rentCost",
            1200,
            "numOfBedrooms",
            3
        );

        mockMvc
            .perform(
                post("/api/households")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.householdName").value("Test House"));
    }

    @Test
    void updateHousehold_returns200_withUpdatedDTO() throws Exception {
        HouseholdDTO updatedDTO = new HouseholdDTO(
            1L,
            "Updated House",
            1500f,
            3,
            "123 Main St",
            "Carmel",
            "CA",
            "93923",
            "US"
        );
        when(
            householdService.updateHousehold(eq(1L), any(Household.class))
        ).thenReturn(updatedDTO);

        mockMvc
            .perform(
                put("/api/households/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testHousehold))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.householdName").value("Updated House"))
            .andExpect(jsonPath("$.rentCost").value(1500.0));
    }

    @Test
    void updateHousehold_returns404_whenNotFound() throws Exception {
        when(
            householdService.updateHousehold(eq(99L), any(Household.class))
        ).thenThrow(new ResourceNotFoundException("Household", 99L));

        mockMvc
            .perform(
                put("/api/households/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testHousehold))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteHousehold_returns204_whenDeleted() throws Exception {
        doNothing().when(householdService).deleteHousehold(1L);

        mockMvc
            .perform(delete("/api/households/1"))
            .andExpect(status().isNoContent());

        verify(householdService, times(1)).deleteHousehold(1L);
    }

    @Test
    void deleteHousehold_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Household", 99L))
            .when(householdService)
            .deleteHousehold(99L);

        mockMvc
            .perform(delete("/api/households/99"))
            .andExpect(status().isNotFound());
    }
}
