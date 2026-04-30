package com.roomie.controller;

import com.roomie.entity.Profile;
import com.roomie.exception.ProfileNotFoundException;
import com.roomie.repository.ProfileRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileRepository profileRepository;

    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    // TODO: create an endpoint for syncing profile data

    @GetMapping("/")
    List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long id) {
        Profile profile = profileRepository
            .findById(id)
            .orElseThrow(() -> new ProfileNotFoundException(id));
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}")
    Profile updateProfile(
        @RequestBody Profile newProfile,
        @PathVariable Long profileId
    ) {
        return profileRepository
            .findById(profileId)
            .map(profile -> {
                profile.setName(newProfile.getName());
                profile.setEmail(newProfile.getEmail());
                profile.setAge(newProfile.getAge());
                profile.setProfilePicUrl(newProfile.getProfilePicUrl());
                return profileRepository.save(profile);
            })
            .orElseThrow(() -> new ProfileNotFoundException(profileId));
    }
}
