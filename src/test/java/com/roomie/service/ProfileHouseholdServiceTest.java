package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.enums.ActivityType;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileHouseholdRepository;
import com.roomie.repository.ProfileRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileHouseholdServiceTest {

    @Mock
    private ProfileHouseholdRepository profileHouseholdRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HouseholdRepository householdRepository;

    @Mock
    private ActivityEventService activityEventService;

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ProfileHouseholdService profileHouseholdService;

    private Profile testProfile;
    private Household testHousehold;
    private ProfileHousehold testMembership;

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

        testMembership = new ProfileHousehold();
        setField(testMembership, "profileHouseholdId", 1L);
        testMembership.setProfile(testProfile);
        testMembership.setHousehold(testHousehold);
        testMembership.setPrivs(1);
        testMembership.setPayInterval(3);
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
    void getMembershipById_returnsMembership_whenFound() {
        when(profileHouseholdRepository.findById(1L)).thenReturn(
            Optional.of(testMembership)
        );

        ProfileHousehold result = profileHouseholdService.getMembershipById(1L);

        assertThat(result.getPrivs()).isEqualTo(1);
        assertThat(result.getPayInterval()).isEqualTo(3);
    }

    @Test
    void getMembershipById_throws_whenNotFound() {
        when(profileHouseholdRepository.findById(99L)).thenReturn(
            Optional.empty()
        );

        assertThatThrownBy(() ->
            profileHouseholdService.getMembershipById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getMembershipByProfileAndHousehold_returnsMembership_whenFound() {
        when(
            profileHouseholdRepository.findByProfileProfileIdAndHouseholdHouseholdId(
                1L,
                1L
            )
        ).thenReturn(Optional.of(testMembership));

        ProfileHousehold result =
            profileHouseholdService.getMembershipByProfileAndHousehold(1L, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getProfile().getProfileId()).isEqualTo(1L);
    }

    @Test
    void getMembershipByProfileAndHousehold_throws_whenNotFound() {
        when(
            profileHouseholdRepository.findByProfileProfileIdAndHouseholdHouseholdId(
                99L,
                99L
            )
        ).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            profileHouseholdService.getMembershipByProfileAndHousehold(99L, 99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addMember_savesAndReturnsMembership() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(profileHouseholdRepository.save(any())).thenReturn(testMembership);

        ProfileHousehold result = profileHouseholdService.addMember(
            1L,
            1L,
            1,
            3
        );

        assertThat(result).isNotNull();
        verify(profileHouseholdRepository, times(1)).save(any());
    }

    @Test
    void addMember_recalculatesExpenses_afterSave() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(profileHouseholdRepository.save(any())).thenReturn(testMembership);

        profileHouseholdService.addMember(1L, 1L, 1, 3);

        verify(expenseService, times(1)).recalculateAllHouseholdExpenses(any());
    }

    @Test
    void addMember_logsJoinedActivity() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(profileHouseholdRepository.save(any())).thenReturn(testMembership);

        profileHouseholdService.addMember(1L, 1L, 1, 3);

        verify(activityEventService, times(1)).log(
            eq(1L),
            eq(1L),
            eq(ActivityType.MEMBER_JOINED),
            eq(false)
        );
    }

    @Test
    void addMember_throws_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            profileHouseholdService.addMember(99L, 1L, 1, 3)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addMember_throws_whenHouseholdNotFound() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            profileHouseholdService.addMember(1L, 99L, 1, 3)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateMember_updatesPrivsAndPayInterval() {
        ProfileHousehold updated = new ProfileHousehold();
        updated.setPrivs(2);
        updated.setPayInterval(6);

        when(profileHouseholdRepository.findById(1L)).thenReturn(
            Optional.of(testMembership)
        );
        when(profileHouseholdRepository.save(any())).thenReturn(testMembership);

        ProfileHousehold result = profileHouseholdService.updateMember(
            1L,
            updated
        );

        assertThat(result).isNotNull();
        verify(profileHouseholdRepository, times(1)).save(any());
    }

    @Test
    void updateMember_throws_whenNotFound() {
        when(profileHouseholdRepository.findById(99L)).thenReturn(
            Optional.empty()
        );

        assertThatThrownBy(() ->
            profileHouseholdService.updateMember(99L, new ProfileHousehold())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removeMember_deletesAndRecalculates() {
        when(profileHouseholdRepository.findById(1L)).thenReturn(
            Optional.of(testMembership)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );

        profileHouseholdService.removeMember(1L);

        verify(profileHouseholdRepository, times(1)).deleteById(1L);
        verify(expenseService, times(1)).recalculateAllHouseholdExpenses(any());
    }

    @Test
    void removeMember_logsLeftActivity() {
        when(profileHouseholdRepository.findById(1L)).thenReturn(
            Optional.of(testMembership)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );

        profileHouseholdService.removeMember(1L);

        verify(activityEventService, times(1)).log(
            eq(1L),
            eq(1L),
            eq(ActivityType.MEMBER_LEFT),
            eq(false)
        );
    }

    @Test
    void removeMember_throws_whenNotFound() {
        when(profileHouseholdRepository.findById(99L)).thenReturn(
            Optional.empty()
        );

        assertThatThrownBy(() ->
            profileHouseholdService.removeMember(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(profileHouseholdRepository, never()).deleteById(any());
    }
}
