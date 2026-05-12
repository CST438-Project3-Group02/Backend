package com.roomie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomie.dto.ChoreDTO;
import com.roomie.entity.Chore;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.service.ChoreService;
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
class ChoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChoreService choreService;

    @Autowired
    private ObjectMapper objectMapper;

    private ChoreDTO testChoreDTO;
    private Chore testChore;

    @BeforeEach
    void setUp() {
        testChore = new Chore("Clean Kitchen", "Wipe surfaces", 7, false, null);
        testChoreDTO = new ChoreDTO(testChore);
    }

    @Test
    void getAllChores_returns200() throws Exception {
        when(choreService.getAllChores()).thenReturn(List.of(testChoreDTO));

        mockMvc
            .perform(get("/api/chores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].choreName").value("Clean Kitchen"));
    }

    @Test
    void getAllChores_returns200_withEmptyList() throws Exception {
        when(choreService.getAllChores()).thenReturn(List.of());

        mockMvc
            .perform(get("/api/chores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getChoreById_returns200_whenFound() throws Exception {
        when(choreService.getChoreById(1L)).thenReturn(testChoreDTO);

        mockMvc
            .perform(get("/api/chores/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.choreName").value("Clean Kitchen"));
    }

    @Test
    void getChoreById_returns404_whenNotFound() throws Exception {
        when(choreService.getChoreById(99L)).thenThrow(
            new ResourceNotFoundException("Chore", 99L)
        );

        mockMvc.perform(get("/api/chores/99")).andExpect(status().isNotFound());
    }

    @Test
    void getChoresByHousehold_returns200() throws Exception {
        when(choreService.getChoresByHousehold(1L)).thenReturn(
            List.of(testChoreDTO)
        );

        mockMvc
            .perform(get("/api/chores/household/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].choreName").value("Clean Kitchen"));
    }

    @Test
    void getChoresByHouseholdAndStatus_returns200() throws Exception {
        when(choreService.getChoresByHouseholdAndStatus(1L, false)).thenReturn(
            List.of(testChoreDTO)
        );

        mockMvc
            .perform(
                get("/api/chores/household/1/status").param(
                    "completed",
                    "false"
                )
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    void getChoresByProfile_returns200() throws Exception {
        when(choreService.getChoresByProfile(1L)).thenReturn(
            List.of(testChoreDTO)
        );

        mockMvc
            .perform(get("/api/chores/profile/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].choreName").value("Clean Kitchen"));
    }

    @Test
    void createChore_returns201() throws Exception {
        when(
            choreService.createChore(eq(1L), eq(1L), any(Chore.class))
        ).thenReturn(testChore);
        when(choreService.toDTO(any())).thenReturn(testChoreDTO);

        Map<String, Object> payload = Map.of(
            "profileId",
            1,
            "householdId",
            1,
            "choreName",
            "Clean Kitchen",
            "choreDescription",
            "Wipe surfaces",
            "repeatInterval",
            7
        );

        mockMvc
            .perform(
                post("/api/chores")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.choreName").value("Clean Kitchen"));
    }

    @Test
    void createChore_returns404_whenProfileNotFound() throws Exception {
        when(
            choreService.createChore(eq(99L), eq(1L), any(Chore.class))
        ).thenThrow(new ResourceNotFoundException("Profile", 99L));

        Map<String, Object> payload = Map.of(
            "profileId",
            99,
            "householdId",
            1,
            "choreName",
            "Clean Kitchen",
            "choreDescription",
            "Wipe surfaces",
            "repeatInterval",
            7
        );

        mockMvc
            .perform(
                post("/api/chores")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void markChoreComplete_returns200() throws Exception {
        when(choreService.markComplete(1L)).thenReturn(testChoreDTO);

        mockMvc
            .perform(patch("/api/chores/1/complete"))
            .andExpect(status().isOk());
    }

    @Test
    void markChoreComplete_returns404_whenNotFound() throws Exception {
        when(choreService.markComplete(99L)).thenThrow(
            new ResourceNotFoundException("Chore", 99L)
        );

        mockMvc
            .perform(patch("/api/chores/99/complete"))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteChore_returns204() throws Exception {
        doNothing().when(choreService).deleteChore(1L);

        mockMvc
            .perform(delete("/api/chores/1"))
            .andExpect(status().isNoContent());

        verify(choreService, times(1)).deleteChore(1L);
    }

    @Test
    void deleteChore_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Chore", 99L))
            .when(choreService)
            .deleteChore(99L);

        mockMvc
            .perform(delete("/api/chores/99"))
            .andExpect(status().isNotFound());
    }
}
