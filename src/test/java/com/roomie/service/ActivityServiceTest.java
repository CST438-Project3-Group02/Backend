package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.dto.ActivityDTO;
import com.roomie.entity.Activity;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ActivityRepository;
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
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HouseholdRepository householdRepository;

    @InjectMocks
    private ActivityService activityService;

    private Activity testActivity;
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

        testActivity = new Activity(1, false);
        setField(testActivity, "activityId", 1L);
        testActivity.setProfile(testProfile);
        testActivity.setHousehold(testHousehold);
        testActivity.setPostComment("Chore completed!");
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

    @Test
    void getAllActivities_returnsListOfDTOs() {
        when(activityRepository.findAll()).thenReturn(List.of(testActivity));

        List<ActivityDTO> result = activityService.getAllActivities();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActivityType()).isEqualTo(1);
        assertThat(result.get(0).getPostComment()).isEqualTo(
            "Chore completed!"
        );
    }

    @Test
    void getAllActivities_returnsEmptyList_whenNone() {
        when(activityRepository.findAll()).thenReturn(List.of());

        List<ActivityDTO> result = activityService.getAllActivities();

        assertThat(result).isEmpty();
    }

    @Test
    void getActivityById_returnsDTO_whenFound() {
        when(activityRepository.findById(1L)).thenReturn(
            Optional.of(testActivity)
        );

        ActivityDTO result = activityService.getActivityById(1L);

        assertThat(result.getActivityId()).isEqualTo(1L);
        assertThat(result.isCompleted()).isFalse();
    }

    @Test
    void getActivityById_throws_whenNotFound() {
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            activityService.getActivityById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getActivityFeedByHousehold_returnsDTOs() {
        when(activityRepository.findByHousehold_HouseholdId(1L)).thenReturn(
            List.of(testActivity)
        );

        List<ActivityDTO> result = activityService.getActivityFeedByHousehold(
            1L
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActivityType()).isEqualTo(1);
    }

    @Test
    void getActivityFeedByHousehold_returnsEmpty_whenNone() {
        when(activityRepository.findByHousehold_HouseholdId(99L)).thenReturn(
            List.of()
        );

        List<ActivityDTO> result = activityService.getActivityFeedByHousehold(
            99L
        );

        assertThat(result).isEmpty();
    }

    @Test
    void getActivitiesByProfile_returnsDTOs() {
        when(activityRepository.findByProfile_ProfileId(1L)).thenReturn(
            List.of(testActivity)
        );

        List<ActivityDTO> result = activityService.getActivitiesByProfile(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void createActivity_savesAndReturnsActivity() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(activityRepository.save(any())).thenReturn(testActivity);

        Activity result = activityService.createActivity(
            1L,
            1L,
            1,
            "comment",
            null
        );

        assertThat(result).isNotNull();
        assertThat(result.getActivityType()).isEqualTo(1);
        verify(activityRepository, times(1)).save(any());
    }

    @Test
    void createActivity_throws_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            activityService.createActivity(99L, 1L, 1, null, null)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createActivity_throws_whenHouseholdNotFound() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            activityService.createActivity(1L, 99L, 1, null, null)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateActivity_updatesAndReturnsDTO() {
        Activity updated = new Activity(2, true);
        when(activityRepository.findById(1L)).thenReturn(
            Optional.of(testActivity)
        );
        when(activityRepository.save(any())).thenReturn(testActivity);

        ActivityDTO result = activityService.updateActivity(1L, updated);

        assertThat(result).isNotNull();
        verify(activityRepository, times(1)).save(any());
    }

    @Test
    void updateActivity_throws_whenNotFound() {
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            activityService.updateActivity(99L, new Activity())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteActivity_deletesWhenExists() {
        when(activityRepository.existsById(1L)).thenReturn(true);

        activityService.deleteActivity(1L);

        verify(activityRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteActivity_throws_whenNotFound() {
        when(activityRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() ->
            activityService.deleteActivity(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(activityRepository, never()).deleteById(any());
    }

    @Test
    void toDTO_mapsAllFieldsCorrectly() {
        ActivityDTO dto = activityService.toDTO(testActivity);

        assertThat(dto.getActivityId()).isEqualTo(1L);
        assertThat(dto.getActivityType()).isEqualTo(1);
        assertThat(dto.isCompleted()).isFalse();
        assertThat(dto.getPostComment()).isEqualTo("Chore completed!");
        assertThat(dto.getProfile().getName()).isEqualTo("John Doe");
    }
}
