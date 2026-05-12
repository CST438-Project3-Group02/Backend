package com.roomie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomie.dto.ProfileDTO;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.service.ProfileService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfileDTO testProfileDTO;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testProfileDTO = new ProfileDTO(
            1L,
            "John Doe",
            "john@example.com",
            25,
            null
        );
        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
    }

    @Test
    void getAllProfiles_returns200_withListOfProfiles() throws Exception {
        when(profileService.getAllProfiles()).thenReturn(
            List.of(testProfileDTO)
        );

        mockMvc
            .perform(get("/api/profiles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("John Doe"))
            .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void getAllProfiles_returns200_withEmptyList() throws Exception {
        when(profileService.getAllProfiles()).thenReturn(List.of());

        mockMvc
            .perform(get("/api/profiles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getProfileById_returns200_whenProfileExists() throws Exception {
        when(profileService.getProfileById(1L)).thenReturn(testProfileDTO);

        mockMvc
            .perform(get("/api/profiles/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"))
            .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void getProfileById_returns404_whenProfileNotFound() throws Exception {
        when(profileService.getProfileById(99L)).thenThrow(
            new ResourceNotFoundException("Profile", 99L)
        );

        mockMvc
            .perform(get("/api/profiles/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getProfileByOauthId_returns200_whenProfileExists() throws Exception {
        when(profileService.getProfileByOauthId("oauth-123")).thenReturn(
            testProfileDTO
        );

        mockMvc
            .perform(get("/api/profiles/oauth/oauth-123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getProfileByOauthId_returns404_whenNotFound() throws Exception {
        when(profileService.getProfileByOauthId("bad-oauth")).thenThrow(
            new ResourceNotFoundException("Profile", null)
        );

        mockMvc
            .perform(get("/api/profiles/oauth/bad-oauth"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createProfile_returns201_withCreatedProfile() throws Exception {
        when(profileService.createProfile(any(Profile.class))).thenReturn(
            testProfile
        );

        mockMvc
            .perform(
                post("/api/profiles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testProfile))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void syncProfile_returns200_withSyncedProfile() throws Exception {
        Map<String, Object> record = Map.of(
            "id",
            "oauth-123",
            "email",
            "john@example.com"
        );
        Map<String, Object> payload = Map.of("record", record);

        when(profileService.syncProfile(any())).thenReturn(testProfile);

        mockMvc
            .perform(
                post("/api/profiles/sync")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void updateProfile_returns200_withUpdatedProfile() throws Exception {
        ProfileDTO updatedDTO = new ProfileDTO(
            1L,
            "Jane Doe",
            "jane@example.com",
            30,
            null
        );
        when(
            profileService.updateProfile(eq(1L), any(Profile.class))
        ).thenReturn(updatedDTO);

        Profile updatePayload = new Profile(
            "Jane Doe",
            "jane@example.com",
            30,
            "oauth-123"
        );

        mockMvc
            .perform(
                put("/api/profiles/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatePayload))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Jane Doe"))
            .andExpect(jsonPath("$.email").value("jane@example.com"))
            .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void updateProfile_returns404_whenProfileNotFound() throws Exception {
        when(
            profileService.updateProfile(eq(99L), any(Profile.class))
        ).thenThrow(new ResourceNotFoundException("Profile", 99L));

        mockMvc
            .perform(
                put("/api/profiles/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testProfile))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteProfile_returns204_whenProfileDeleted() throws Exception {
        doNothing().when(profileService).deleteProfile(1L);

        mockMvc
            .perform(delete("/api/profiles/1"))
            .andExpect(status().isNoContent());

        verify(profileService, times(1)).deleteProfile(1L);
    }

    @Test
    void deleteProfile_returns404_whenProfileNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Profile", 99L))
            .when(profileService)
            .deleteProfile(99L);

        mockMvc
            .perform(delete("/api/profiles/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getProfileWithHouseholds_returns200() throws Exception {
        when(profileService.getProfileWithHouseholds(1L)).thenReturn(
            testProfileDTO
        );

        mockMvc
            .perform(get("/api/profiles/1/households"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getProfileWithHouseholds_returns404_whenNotFound() throws Exception {
        when(profileService.getProfileWithHouseholds(99L)).thenThrow(
            new ResourceNotFoundException("Profile", 99L)
        );

        mockMvc
            .perform(get("/api/profiles/99/households"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getProfileWithChores_returns200() throws Exception {
        when(profileService.getProfileWithChores(1L)).thenReturn(
            testProfileDTO
        );

        mockMvc
            .perform(get("/api/profiles/1/chores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getProfileWithBills_returns200() throws Exception {
        when(profileService.getProfileWithBills(1L)).thenReturn(testProfileDTO);

        mockMvc
            .perform(get("/api/profiles/1/bills"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getProfileWithActivities_returns200() throws Exception {
        when(profileService.getProfileWithActivities(1L)).thenReturn(
            testProfileDTO
        );

        mockMvc
            .perform(get("/api/profiles/1/activities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }
}
