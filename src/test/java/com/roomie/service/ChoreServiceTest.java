package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.dto.ChoreDTO;
import com.roomie.entity.Chore;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.enums.ActivityType;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ChoreRepository;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChoreServiceTest {

    @Mock
    private ChoreRepository choreRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HouseholdRepository householdRepository;

    @Mock
    private ActivityEventService activityEventService;

    @InjectMocks
    private ChoreService choreService;

    private Chore testChore;
    private Profile testProfile;
    private Household testHousehold;

    @BeforeEach
    void setUp() {
        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        setField(testProfile, "profileId", 1L);

        testHousehold = new Household();
        setField(testHousehold, "householdId", 1L);
        testHousehold.setHouseholdName("Test House");

        testChore = new Chore(
            "Clean Kitchen",
            "Wipe all surfaces",
            7,
            false,
            null
        );
        setField(testChore, "choreId", 1L);
        testChore.setProfile(testProfile);
        testChore.setHousehold(testHousehold);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ─── getAllChores ─────────────────────────────────────────────────────────

    @Test
    void getAllChores_returnsListOfDTOs() {
        when(choreRepository.findAll()).thenReturn(List.of(testChore));

        List<ChoreDTO> result = choreService.getAllChores();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getChoreName()).isEqualTo("Clean Kitchen");
    }

    @Test
    void getAllChores_returnsEmptyList_whenNone() {
        when(choreRepository.findAll()).thenReturn(List.of());

        assertThat(choreService.getAllChores()).isEmpty();
    }

    // ─── getChoreById ─────────────────────────────────────────────────────────

    @Test
    void getChoreById_returnsDTO_whenFound() {
        when(choreRepository.findById(1L)).thenReturn(Optional.of(testChore));

        ChoreDTO result = choreService.getChoreById(1L);

        assertThat(result.getChoreName()).isEqualTo("Clean Kitchen");
        assertThat(result.isCompleted()).isFalse();
    }

    @Test
    void getChoreById_throws_whenNotFound() {
        when(choreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> choreService.getChoreById(99L)).isInstanceOf(
            ResourceNotFoundException.class
        );
    }

    // ─── getChoresByHousehold ─────────────────────────────────────────────────

    @Test
    void getChoresByHousehold_returnsDTOs() {
        when(choreRepository.findByHousehold_HouseholdId(1L)).thenReturn(
            List.of(testChore)
        );

        List<ChoreDTO> result = choreService.getChoresByHousehold(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHouseholdId()).isEqualTo(1L);
    }

    // ─── getChoresByProfile ───────────────────────────────────────────────────

    @Test
    void getChoresByProfile_returnsDTOs() {
        when(choreRepository.findByProfile_ProfileId(1L)).thenReturn(
            List.of(testChore)
        );

        List<ChoreDTO> result = choreService.getChoresByProfile(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProfileId()).isEqualTo(1L);
    }

    // ─── getChoresByHouseholdAndStatus ────────────────────────────────────────

    @Test
    void getChoresByHouseholdAndStatus_returnsFilteredDTOs() {
        when(
            choreRepository.findByHousehold_HouseholdIdAndIsCompleted(1L, false)
        ).thenReturn(List.of(testChore));

        List<ChoreDTO> result = choreService.getChoresByHouseholdAndStatus(
            1L,
            false
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isCompleted()).isFalse();
    }

    // ─── createChore ──────────────────────────────────────────────────────────

    @Test
    void createChore_savesAndReturnsChore() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(choreRepository.save(any())).thenReturn(testChore);

        Chore result = choreService.createChore(1L, 1L, testChore);

        assertThat(result.getChoreName()).isEqualTo("Clean Kitchen");
        verify(choreRepository, times(1)).save(any());
    }

    @Test
    void createChore_logsCreatedActivity() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(choreRepository.save(any())).thenReturn(testChore);

        choreService.createChore(1L, 1L, testChore);

        verify(activityEventService, times(1)).log(
            eq(1L),
            eq(1L),
            eq(ActivityType.CHORE_CREATED),
            eq(false)
        );
    }

    @Test
    void createChore_throws_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            choreService.createChore(99L, 1L, testChore)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createChore_throws_whenHouseholdNotFound() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            choreService.createChore(1L, 99L, testChore)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    // ─── updateChore ──────────────────────────────────────────────────────────

    @Test
    void updateChore_updatesFieldsAndReturnsDTO() {
        Chore updated = new Chore(
            "Mop Floor",
            "Mop all floors",
            7,
            false,
            null
        );
        when(choreRepository.findById(1L)).thenReturn(Optional.of(testChore));
        when(choreRepository.save(any())).thenReturn(testChore);

        ChoreDTO result = choreService.updateChore(1L, updated);

        assertThat(result).isNotNull();
        verify(choreRepository, times(1)).save(any());
    }

    @Test
    void updateChore_throws_whenNotFound() {
        when(choreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            choreService.updateChore(99L, new Chore())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    // ─── markComplete ─────────────────────────────────────────────────────────

    @Test
    void markComplete_setsCompletedTrueAndReturnsDTO() {
        when(choreRepository.findById(1L)).thenReturn(Optional.of(testChore));
        when(choreRepository.save(any())).thenReturn(testChore);

        ChoreDTO result = choreService.markComplete(1L);

        assertThat(result).isNotNull();
        verify(choreRepository, times(1)).save(any());
    }

    @Test
    void markComplete_logsCompletedActivity() {
        when(choreRepository.findById(1L)).thenReturn(Optional.of(testChore));
        when(choreRepository.save(any())).thenReturn(testChore);

        choreService.markComplete(1L);

        verify(activityEventService, times(1)).log(
            eq(1L),
            eq(1L),
            eq(ActivityType.CHORE_COMPLETED),
            eq(true)
        );
    }

    @Test
    void markComplete_throws_whenNotFound() {
        when(choreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> choreService.markComplete(99L)).isInstanceOf(
            ResourceNotFoundException.class
        );
    }

    // ─── deleteChore ──────────────────────────────────────────────────────────

    @Test
    void deleteChore_deletesWhenExists() {
        when(choreRepository.existsById(1L)).thenReturn(true);

        choreService.deleteChore(1L);

        verify(choreRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteChore_throws_whenNotFound() {
        when(choreRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> choreService.deleteChore(99L)).isInstanceOf(
            ResourceNotFoundException.class
        );

        verify(choreRepository, never()).deleteById(any());
    }
}
