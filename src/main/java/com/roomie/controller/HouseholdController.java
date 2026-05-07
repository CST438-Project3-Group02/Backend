package com.roomie.controller;

import com.roomie.dto.HouseholdDTO;
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
    public ResponseEntity<List<HouseholdDTO>> getAllHouseholds() {
        return ResponseEntity.ok(householdService.getAllHouseholds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseholdDTO> getHouseholdById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(householdService.getHouseholdById(id));
    }

    @GetMapping("/{id}/profiles")
    public ResponseEntity<HouseholdDTO> getHouseholdWithProfiles(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(householdService.getHouseholdWithProfiles(id));
    }

    @GetMapping("/{id}/chores")
    public ResponseEntity<HouseholdDTO> getHouseholdWithChores(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(householdService.getHouseholdWithChores(id));
    }

    @GetMapping("/{id}/grocery-lists")
    public ResponseEntity<HouseholdDTO> getHouseholdWithGroceryLists(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            householdService.getHouseholdWithGroceryLists(id)
        );
    }

    @GetMapping("/{id}/expenses")
    public ResponseEntity<HouseholdDTO> getHouseholdWithExpenses(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(householdService.getHouseholdWithExpenses(id));
    }

    @GetMapping("/{id}/invites")
    public ResponseEntity<HouseholdDTO> getHouseholdWithInvites(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(householdService.getHouseholdWithInvites(id));
    }

    @PostMapping
    public ResponseEntity<HouseholdDTO> createHousehold(
        @RequestBody Map<String, Object> payload
    ) {
        Long profileId = ((Number) payload.get("profileId")).longValue();

        Household household = new Household();

        // strings — only set if present and non-empty
        if (payload.get("householdName") != null) household.setHouseholdName(
            (String) payload.get("householdName")
        );
        if (
            payload.get("address") != null &&
            !payload.get("address").toString().isEmpty()
        ) household.setAddress((String) payload.get("address"));
        if (
            payload.get("city") != null &&
            !payload.get("city").toString().isEmpty()
        ) household.setCity((String) payload.get("city"));
        if (
            payload.get("state") != null &&
            !payload.get("state").toString().isEmpty()
        ) household.setState((String) payload.get("state"));
        if (
            payload.get("zipCode") != null &&
            !payload.get("zipCode").toString().isEmpty()
        ) household.setZipCode((String) payload.get("zipCode"));
        if (
            payload.get("country") != null &&
            !payload.get("country").toString().isEmpty()
        ) household.setCountry((String) payload.get("country"));

        // numbers — only set if present and non-null
        if (payload.get("rentCost") != null) household.setRentCost(
            ((Number) payload.get("rentCost")).floatValue()
        );
        if (payload.get("numOfBedrooms") != null) household.setNumOfBedrooms(
            ((Number) payload.get("numOfBedrooms")).intValue()
        );

        Household created = householdService.createHousehold(
            household,
            profileId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
            householdService.toDTO(created)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<HouseholdDTO> updateHousehold(
        @PathVariable Long id,
        @RequestBody Household household
    ) {
        return ResponseEntity.ok(
            householdService.updateHousehold(id, household)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHousehold(@PathVariable Long id) {
        householdService.deleteHousehold(id);
        return ResponseEntity.noContent().build();
    }
}
