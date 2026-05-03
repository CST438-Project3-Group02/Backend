package com.roomie.controller;

import com.roomie.dto.ProfileDTO;
import com.roomie.entity.Profile;
import com.roomie.service.ProfileService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    @GetMapping("/oauth/{oauthId}")
    public ResponseEntity<ProfileDTO> getProfileByOauthId(
        @PathVariable String oauthId
    ) {
        return ResponseEntity.ok(profileService.getProfileByOauthId(oauthId));
    }

    @GetMapping("/{id}/households")
    public ResponseEntity<ProfileDTO> getProfileWithHouseholds(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileService.getProfileWithHouseholds(id));
    }

    @GetMapping("/{id}/chores")
    public ResponseEntity<ProfileDTO> getProfileWithChores(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileService.getProfileWithChores(id));
    }

    @GetMapping("/{id}/bills")
    public ResponseEntity<ProfileDTO> getProfileWithBills(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileService.getProfileWithBills(id));
    }

    @GetMapping("/{id}/grocery-lists")
    public ResponseEntity<ProfileDTO> getProfileWithGroceryLists(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileService.getProfileWithGroceryLists(id));
    }

    @GetMapping("/{id}/grocery-items")
    public ResponseEntity<ProfileDTO> getProfileWithGroceryItems(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileService.getProfileWithGroceryItems(id));
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<ProfileDTO> getProfileWithActivities(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileService.getProfileWithActivities(id));
    }

    @GetMapping("/{id}/activity-comments")
    public ResponseEntity<ProfileDTO> getProfileWithActivityComments(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            profileService.getProfileWithActivityComments(id)
        );
    }

    @GetMapping("/{id}/activity-reactions")
    public ResponseEntity<ProfileDTO> getProfileWithActivityReactions(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            profileService.getProfileWithActivityReactions(id)
        );
    }

    @GetMapping("/{id}/invites")
    public ResponseEntity<ProfileDTO> getProfileWithInvites(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileService.getProfileWithInvites(id));
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            profileService.createProfile(profile)
        );
    }

    @PostMapping("/sync")
    public ResponseEntity<Profile> syncProfile(
        @RequestBody Map<String, Object> payload
    ) {
        return ResponseEntity.ok(profileService.syncProfile(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> updateProfile(
        @PathVariable Long id,
        @RequestBody Profile profile
    ) {
        return ResponseEntity.ok(profileService.updateProfile(id, profile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
