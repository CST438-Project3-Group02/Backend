package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.dto.ProfileDTO;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        setProfileId(testProfile, 1L);
    }

    private void setProfileId(Profile profile, Long id) {
        try {
            var field = Profile.class.getDeclaredField("profileId");
            field.setAccessible(true);
            field.set(profile, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllProfiles_returnsListOfDTOs() {
        when(profileRepository.findAll()).thenReturn(List.of(testProfile));

        List<ProfileDTO> result = profileService.getAllProfiles();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(0).getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void getAllProfiles_returnsEmptyList_whenNoProfiles() {
        when(profileRepository.findAll()).thenReturn(List.of());

        List<ProfileDTO> result = profileService.getAllProfiles();

        assertThat(result).isEmpty();
    }

    @Test
    void getProfileById_returnsDTO_whenProfileExists() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );

        ProfileDTO result = profileService.getProfileById(1L);

        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getAge()).isEqualTo(25);
    }

    @Test
    void getProfileById_throwsResourceNotFoundException_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            profileService.getProfileById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getProfileByOauthId_returnsDTO_whenProfileExists() {
        when(profileRepository.findByOauthId("oauth-123")).thenReturn(
            Optional.of(testProfile)
        );

        ProfileDTO result = profileService.getProfileByOauthId("oauth-123");

        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    void getProfileByOauthId_throwsResourceNotFoundException_whenNotFound() {
        when(profileRepository.findByOauthId("bad-oauth")).thenReturn(
            Optional.empty()
        );

        assertThatThrownBy(() ->
            profileService.getProfileByOauthId("bad-oauth")
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createProfile_savesAndReturnsProfile() {
        when(profileRepository.save(any(Profile.class))).thenReturn(
            testProfile
        );

        Profile result = profileService.createProfile(testProfile);

        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(profileRepository, times(1)).save(testProfile);
    }

    @Test
    void updateProfile_updatesFieldsAndReturnsDTO() {
        Profile updatedProfile = new Profile(
            "Jane Doe",
            "jane@example.com",
            30,
            "oauth-123"
        );

        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(profileRepository.save(any(Profile.class))).thenReturn(
            testProfile
        );

        ProfileDTO result = profileService.updateProfile(1L, updatedProfile);

        assertThat(result.getName()).isEqualTo("Jane Doe");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        assertThat(result.getAge()).isEqualTo(30);
    }

    @Test
    void updateProfile_throwsResourceNotFoundException_whenProfileNotFound() {
        Profile updatedProfile = new Profile(
            "Jane Doe",
            "jane@example.com",
            30,
            "oauth-123"
        );
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            profileService.updateProfile(99L, updatedProfile)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteProfile_deletesProfile_whenExists() {
        when(profileRepository.existsById(1L)).thenReturn(true);

        profileService.deleteProfile(1L);

        verify(profileRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProfile_throwsResourceNotFoundException_whenNotFound() {
        when(profileRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() ->
            profileService.deleteProfile(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(profileRepository, never()).deleteById(any());
    }

    @Test
    void syncProfile_createsNewProfile_whenOauthIdDoesNotExist() {
        Map<String, Object> record = Map.of(
            "id",
            "new-oauth-456",
            "email",
            "newuser@example.com"
        );
        Map<String, Object> payload = Map.of("record", record);

        when(profileRepository.existsByOauthId("new-oauth-456")).thenReturn(
            false
        );
        when(profileRepository.save(any(Profile.class))).thenReturn(
            testProfile
        );

        Profile result = profileService.syncProfile(payload);

        assertThat(result).isNotNull();
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void syncProfile_returnsExistingProfile_whenOauthIdExists() {
        Map<String, Object> record = Map.of(
            "id",
            "oauth-123",
            "email",
            "john@example.com"
        );
        Map<String, Object> payload = Map.of("record", record);

        when(profileRepository.existsByOauthId("oauth-123")).thenReturn(true);
        when(profileRepository.findByOauthId("oauth-123")).thenReturn(
            Optional.of(testProfile)
        );

        Profile result = profileService.syncProfile(payload);

        assertThat(result.getName()).isEqualTo("John Doe");
        verify(profileRepository, never()).save(any());
    }

    @Test
    void syncProfile_usesEmailPrefix_asName() {
        Map<String, Object> record = Map.of(
            "id",
            "new-oauth-789",
            "email",
            "alice@example.com"
        );
        Map<String, Object> payload = Map.of("record", record);

        when(profileRepository.existsByOauthId("new-oauth-789")).thenReturn(
            false
        );
        when(profileRepository.save(any(Profile.class))).thenAnswer(
            invocation -> {
                Profile saved = invocation.getArgument(0);
                assertThat(saved.getName()).isEqualTo("alice");
                return saved;
            }
        );

        profileService.syncProfile(payload);
    }
}
