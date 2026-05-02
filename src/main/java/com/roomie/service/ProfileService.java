package com.roomie.service;

import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile syncProfile(Map<String, Object> entry) {
        Map<String, Object> record = (Map<String, Object>) entry.get("record");

        String oauthId = (String) record.get("id");
        String email = (String) record.get("email");
        String name = "user";

        if (email != null)
            name = email.split("@")[0];

        if (profileRepository.existsByOauthId(oauthId)) {
            return profileRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", null));
        }

        Profile profile = new Profile(name, email, null, oauthId);

        return profileRepository.save(profile);
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> getProfileByOauthId(String oauthId) { return profileRepository.findByOauthId(oauthId); }

    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long id, Profile updatedProfile) {
        Profile original = profileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        original.setName(updatedProfile.getName());
        original.setEmail(updatedProfile.getEmail());
        original.setAge(updatedProfile.getAge());
        original.setProfilePicUrl(updatedProfile.getProfilePicUrl());

        return profileRepository.save(original);
    }

    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profile", id);
        }
        profileRepository.deleteById(id);
    }

}
