package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.entity.Activity;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.enums.ActivityType;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ActivityRepository;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActivityEventServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HouseholdRepository householdRepository;

    @InjectMocks
    private ActivityEventService activityEventService;

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
    void log_savesActivityWithCorrectType() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(activityRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        activityEventService.log(1L, 1L, ActivityType.CHORE_COMPLETED, true);

        ArgumentCaptor<Activity> captor = ArgumentCaptor.forClass(
            Activity.class
        );
        verify(activityRepository, times(1)).save(captor.capture());

        Activity saved = captor.getValue();
        assertThat(saved.getActivityType()).isEqualTo(
            ActivityType.CHORE_COMPLETED.getValue()
        );
        assertThat(saved.isCompleted()).isTrue();
        assertThat(saved.getProfile()).isEqualTo(testProfile);
        assertThat(saved.getHousehold()).isEqualTo(testHousehold);
    }

    @Test
    void log_savesActivityWithIsCompleted_false() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(activityRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        activityEventService.log(1L, 1L, ActivityType.MEMBER_JOINED, false);

        ArgumentCaptor<Activity> captor = ArgumentCaptor.forClass(
            Activity.class
        );
        verify(activityRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().isCompleted()).isFalse();
        assertThat(captor.getValue().getActivityType()).isEqualTo(
            ActivityType.MEMBER_JOINED.getValue()
        );
    }

    @Test
    void log_throws_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            activityEventService.log(
                99L,
                1L,
                ActivityType.CHORE_COMPLETED,
                false
            )
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(activityRepository, never()).save(any());
    }

    @Test
    void log_throws_whenHouseholdNotFound() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            activityEventService.log(
                1L,
                99L,
                ActivityType.CHORE_COMPLETED,
                false
            )
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(activityRepository, never()).save(any());
    }

    @Test
    void log_handlesAllActivityTypes() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(activityRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        for (ActivityType type : ActivityType.values()) {
            activityEventService.log(1L, 1L, type, false);
        }

        verify(activityRepository, times(ActivityType.values().length)).save(
            any()
        );
    }
}
