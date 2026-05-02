package com.roomie.service;

import com.roomie.dto.ProfileDTO;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    private ProfileDTO toDTO(Profile profile) {
        return new ProfileDTO(
            profile.getProfileId(),
            profile.getName(),
            profile.getEmail(),
            profile.getAge(),
            profile.getProfilePicUrl()
        );
    }

    public List<ProfileDTO> getAllProfiles() {
        return profileRepository
            .findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public ProfileDTO getProfileById(Long id) {
        Profile profile = profileRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        return toDTO(profile);
    }

    public ProfileDTO getProfileByOauthId(String oauthId) {
        Profile profile = profileRepository
            .findByOauthId(oauthId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", null));
        return toDTO(profile);
    }

    public ProfileDTO getProfileWithHouseholds(Long id) {
        Profile profile = profileRepository
            .findProfileWithHouseholds(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setHouseholds(
            profile
                .getProfileHouseholds()
                .stream()
                .map(ProfileHousehold::getHousehold)
                .collect(Collectors.toList())
        );
        return dto;
    }

    public ProfileDTO getProfileWithChores(Long id) {
        Profile profile = profileRepository
            .findProfileWithChores(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setChores(profile.getChores());
        return dto;
    }

    public ProfileDTO getProfileWithBills(Long id) {
        Profile profile = profileRepository
            .findProfileWithBills(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setBills(profile.getBills());
        return dto;
    }

    public ProfileDTO getProfileWithGroceryLists(Long id) {
        Profile profile = profileRepository
            .findProfileWithGroceryLists(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setGroceryLists(profile.getGroceryLists());
        return dto;
    }

    public ProfileDTO getProfileWithGroceryItems(Long id) {
        Profile profile = profileRepository
            .findProfileWithGroceryItems(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setGroceryItems(profile.getGroceryItems());
        return dto;
    }

    public ProfileDTO getProfileWithActivities(Long id) {
        Profile profile = profileRepository
            .findProfileWithActivities(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setActivities(profile.getActivities());
        return dto;
    }

    public ProfileDTO getProfileWithActivityComments(Long id) {
        Profile profile = profileRepository
            .findProfileWithActivityComments(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setActivityComments(profile.getActivityComments());
        return dto;
    }

    public ProfileDTO getProfileWithActivityReactions(Long id) {
        Profile profile = profileRepository
            .findProfileWithActivityReactions(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setActivityReactions(profile.getActivityReactions());
        return dto;
    }

    public ProfileDTO getProfileWithInvites(Long id) {
        Profile profile = profileRepository
            .findProfileWithInvites(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        ProfileDTO dto = toDTO(profile);
        dto.setInvites(profile.getInvites());
        return dto;
    }

    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public ProfileDTO updateProfile(Long id, Profile updatedProfile) {
        Profile existing = profileRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        existing.setName(updatedProfile.getName());
        existing.setEmail(updatedProfile.getEmail());
        existing.setAge(updatedProfile.getAge());
        existing.setProfilePicUrl(updatedProfile.getProfilePicUrl());
        return toDTO(profileRepository.save(existing));
    }

    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profile", id);
        }
        profileRepository.deleteById(id);
    }

    public Profile syncProfile(Map<String, Object> payload) {
        Map<String, Object> record = (Map<String, Object>) payload.get(
            "record"
        );
        String oauthId = (String) record.get("id");
        String email = (String) record.get("email");
        String name = email != null ? email.split("@")[0] : "user";
        if (profileRepository.existsByOauthId(oauthId)) {
            return profileRepository
                .findByOauthId(oauthId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Profile", null)
                );
        }
        Profile profile = new Profile(name, email, null, oauthId);
        return profileRepository.save(profile);
    }
}
