package com.roomie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomie.dto.ActivityDTO;
import com.roomie.entity.Activity;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.service.ActivityService;
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
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActivityService activityService;

    @Autowired
    private ObjectMapper objectMapper;

    private ActivityDTO testDTO;
    private Activity testActivity;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        testActivity = new Activity(1, false);
        testActivity.setProfile(testProfile);
        testDTO = new ActivityDTO(
            1L,
            1,
            false,
            testProfile,
            "Chore done!",
            null,
            null
        );
    }

    @Test
    void getAllActivities_returns200_withList() throws Exception {
        when(activityService.getAllActivities()).thenReturn(List.of(testDTO));

        mockMvc
            .perform(get("/api/activities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].activityType").value(1))
            .andExpect(jsonPath("$[0].postComment").value("Chore done!"));
    }

    @Test
    void getAllActivities_returns200_withEmptyList() throws Exception {
        when(activityService.getAllActivities()).thenReturn(List.of());

        mockMvc
            .perform(get("/api/activities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getActivityById_returns200_whenFound() throws Exception {
        when(activityService.getActivityById(1L)).thenReturn(testDTO);

        mockMvc
            .perform(get("/api/activities/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activityId").value(1))
            .andExpect(jsonPath("$.activityType").value(1));
    }

    @Test
    void getActivityById_returns404_whenNotFound() throws Exception {
        when(activityService.getActivityById(99L)).thenThrow(
            new ResourceNotFoundException("Activity", 99L)
        );

        mockMvc
            .perform(get("/api/activities/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getActivityFeedByHousehold_returns200() throws Exception {
        when(activityService.getActivityFeedByHousehold(1L)).thenReturn(
            List.of(testDTO)
        );

        mockMvc
            .perform(get("/api/activities/household/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].activityType").value(1));
    }

    @Test
    void getActivitiesByProfile_returns200() throws Exception {
        when(activityService.getActivitiesByProfile(1L)).thenReturn(
            List.of(testDTO)
        );

        mockMvc
            .perform(get("/api/activities/profile/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].activityType").value(1));
    }

    @Test
    void getActivityWithComments_returns200() throws Exception {
        when(activityService.getActivityWithComments(1L)).thenReturn(testDTO);

        mockMvc
            .perform(get("/api/activities/1/comments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activityId").value(1));
    }

    @Test
    void getActivityWithComments_returns404_whenNotFound() throws Exception {
        when(activityService.getActivityWithComments(99L)).thenThrow(
            new ResourceNotFoundException("Activity", 99L)
        );

        mockMvc
            .perform(get("/api/activities/99/comments"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getActivityWithReactions_returns200() throws Exception {
        when(activityService.getActivityWithReactions(1L)).thenReturn(testDTO);

        mockMvc
            .perform(get("/api/activities/1/reactions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activityId").value(1));
    }

    @Test
    void getActivityWithAll_returns200() throws Exception {
        when(activityService.getActivityWithAll(1L)).thenReturn(testDTO);

        mockMvc
            .perform(get("/api/activities/1/full"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activityId").value(1));
    }

    @Test
    void createActivity_returns201() throws Exception {
        when(
            activityService.createActivity(1L, 1L, 1, "Chore done!", null)
        ).thenReturn(testActivity);
        when(activityService.toDTO(any())).thenReturn(testDTO);

        Map<String, Object> payload = Map.of(
            "profileId",
            1,
            "householdId",
            1,
            "activityType",
            1,
            "postComment",
            "Chore done!"
        );

        mockMvc
            .perform(
                post("/api/activities")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.activityType").value(1));
    }

    @Test
    void createActivity_returns404_whenProfileNotFound() throws Exception {
        when(activityService.createActivity(99L, 1L, 1, null, null)).thenThrow(
            new ResourceNotFoundException("Profile", 99L)
        );

        Map<String, Object> payload = Map.of(
            "profileId",
            99,
            "householdId",
            1,
            "activityType",
            1
        );

        mockMvc
            .perform(
                post("/api/activities")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteActivity_returns204() throws Exception {
        doNothing().when(activityService).deleteActivity(1L);

        mockMvc
            .perform(delete("/api/activities/1"))
            .andExpect(status().isNoContent());

        verify(activityService, times(1)).deleteActivity(1L);
    }

    @Test
    void deleteActivity_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Activity", 99L))
            .when(activityService)
            .deleteActivity(99L);

        mockMvc
            .perform(delete("/api/activities/99"))
            .andExpect(status().isNotFound());
    }
}
