package com.roomie.controller;

import com.roomie.dto.HouseholdDTO;
import com.roomie.entity.Household;
import com.roomie.service.HouseholdService;
import java.util.List;
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
    public ResponseEntity<Household> createHousehold(
        @RequestBody Household household
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            householdService.createHousehold(household)
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
