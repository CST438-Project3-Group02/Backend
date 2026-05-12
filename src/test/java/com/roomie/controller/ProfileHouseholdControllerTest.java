package com.roomie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.service.ProfileHouseholdService;
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
class ProfileHouseholdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileHouseholdService profileHouseholdService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfileHousehold testMembership;

    @BeforeEach
    void setUp() {
        Profile profile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        Household household = new Household();
        household.setHouseholdName("Test House");

        testMembership = new ProfileHousehold();
        testMembership.setPrivs(1);
        testMembership.setPayInterval(3);
        testMembership.setProfile(profile);
        testMembership.setHousehold(household);
    }

    @Test
    void getMembershipById_returns200_whenFound() throws Exception {
        when(profileHouseholdService.getMembershipById(1L)).thenReturn(
            testMembership
        );

        mockMvc
            .perform(get("/api/memberships/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.privs").value(1))
            .andExpect(jsonPath("$.payInterval").value(3));
    }

    @Test
    void getMembershipById_returns404_whenNotFound() throws Exception {
        when(profileHouseholdService.getMembershipById(99L)).thenThrow(
            new ResourceNotFoundException("Membership", 99L)
        );

        mockMvc
            .perform(get("/api/memberships/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getMembershipByProfileAndHousehold_returns200_whenFound()
        throws Exception {
        when(
            profileHouseholdService.getMembershipByProfileAndHousehold(1L, 1L)
        ).thenReturn(testMembership);

        mockMvc
            .perform(
                get("/api/memberships")
                    .param("profileId", "1")
                    .param("householdId", "1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.privs").value(1));
    }

    @Test
    void getMembershipByProfileAndHousehold_returns404_whenNotFound()
        throws Exception {
        when(
            profileHouseholdService.getMembershipByProfileAndHousehold(99L, 99L)
        ).thenThrow(new ResourceNotFoundException("Membership", 99L));

        mockMvc
            .perform(
                get("/api/memberships")
                    .param("profileId", "99")
                    .param("householdId", "99")
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void addMember_returns201_withMembership() throws Exception {
        when(profileHouseholdService.addMember(1L, 1L, 1, 3)).thenReturn(
            testMembership
        );

        Map<String, Object> payload = Map.of(
            "profileId",
            1,
            "householdId",
            1,
            "privs",
            1,
            "payInterval",
            3
        );

        mockMvc
            .perform(
                post("/api/memberships")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.privs").value(1))
            .andExpect(jsonPath("$.payInterval").value(3));
    }

    @Test
    void addMember_returns404_whenProfileNotFound() throws Exception {
        when(profileHouseholdService.addMember(99L, 1L, 1, 3)).thenThrow(
            new ResourceNotFoundException("Profile", 99L)
        );

        Map<String, Object> payload = Map.of(
            "profileId",
            99,
            "householdId",
            1,
            "privs",
            1,
            "payInterval",
            3
        );

        mockMvc
            .perform(
                post("/api/memberships")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void updateMember_returns200_withUpdatedMembership() throws Exception {
        ProfileHousehold updated = new ProfileHousehold();
        updated.setPrivs(2);
        updated.setPayInterval(6);

        when(
            profileHouseholdService.updateMember(
                eq(1L),
                any(ProfileHousehold.class)
            )
        ).thenReturn(updated);

        mockMvc
            .perform(
                put("/api/memberships/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updated))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.privs").value(2))
            .andExpect(jsonPath("$.payInterval").value(6));
    }

    @Test
    void updateMember_returns404_whenNotFound() throws Exception {
        when(
            profileHouseholdService.updateMember(
                eq(99L),
                any(ProfileHousehold.class)
            )
        ).thenThrow(new ResourceNotFoundException("Membership", 99L));

        mockMvc
            .perform(
                put("/api/memberships/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testMembership))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void removeMember_returns204_whenDeleted() throws Exception {
        doNothing().when(profileHouseholdService).removeMember(1L);

        mockMvc
            .perform(delete("/api/memberships/1"))
            .andExpect(status().isNoContent());

        verify(profileHouseholdService, times(1)).removeMember(1L);
    }

    @Test
    void removeMember_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Membership", 99L))
            .when(profileHouseholdService)
            .removeMember(99L);

        mockMvc
            .perform(delete("/api/memberships/99"))
            .andExpect(status().isNotFound());
    }
}
