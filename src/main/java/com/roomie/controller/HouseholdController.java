package com.roomie.controller;

import com.roomie.entity.Household;
import com.roomie.service.HouseholdService;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/households")
public class HouseholdController {

    private final HouseholdService householdService;

    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

    @GetMapping
    public ResponseEntity<List<Household>> getAllHouseholdsByProfile(
            @RequestParam String profileId
    ) {
        System.out.println("PROFILE ID: " + profileId);
        return ResponseEntity.ok(
                householdService.getHouseholdsForUser(profileId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Household> getHouseholdById(@PathVariable Long id) {
        return householdService
            .getHouseholdById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Household> createHousehold(
        @RequestBody Household household
    ) {
        Household createdHousehold = householdService.createHousehold(
            household
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHousehold);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Household> updateHousehold(
        @PathVariable Long id,
        @RequestBody Household household
    ) {
        Household updatedHousehold = householdService.updateHousehold(
            id,
            household
        );
        return ResponseEntity.ok(updatedHousehold);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousehold(@PathVariable Long id) {
        householdService.deleteHousehold(id);
        return ResponseEntity.noContent().build();
    }
}
