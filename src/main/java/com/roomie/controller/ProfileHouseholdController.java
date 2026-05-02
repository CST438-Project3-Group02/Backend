package com.roomie.controller;

import com.roomie.entity.ProfileHousehold;
import com.roomie.service.ProfileHouseholdService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberships")
public class ProfileHouseholdController {

    private final ProfileHouseholdService profileHouseholdService;

    public ProfileHouseholdController(
        ProfileHouseholdService profileHouseholdService
    ) {
        this.profileHouseholdService = profileHouseholdService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileHousehold> getMembershipById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(profileHouseholdService.getMembershipById(id));
    }

    @PostMapping
    public ResponseEntity<ProfileHousehold> addMember(
        @RequestBody Map<String, Object> payload
    ) {
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        Long householdId = Long.valueOf(payload.get("householdId").toString());
        Integer privs = Integer.valueOf(payload.get("privs").toString());
        Integer payInterval = Integer.valueOf(
            payload.get("payInterval").toString()
        );

        ProfileHousehold membership = profileHouseholdService.addMember(
            profileId,
            householdId,
            privs,
            payInterval
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(membership);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileHousehold> updateMember(
        @PathVariable Long id,
        @RequestBody ProfileHousehold updated
    ) {
        return ResponseEntity.ok(
            profileHouseholdService.updateMember(id, updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id) {
        profileHouseholdService.removeMember(id);
        return ResponseEntity.noContent().build();
    }
}
