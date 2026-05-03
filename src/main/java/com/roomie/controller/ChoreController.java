package com.roomie.controller;

import com.roomie.dto.ChoreDTO;
import com.roomie.entity.Chore;
import com.roomie.service.ChoreService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chores")
public class ChoreController {

    private final ChoreService choreService;

    public ChoreController(ChoreService choreService) {
        this.choreService = choreService;
    }

    @GetMapping
    public ResponseEntity<List<ChoreDTO>> getAllChores() {
        return ResponseEntity.ok(choreService.getAllChores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChoreDTO> getChoreById(@PathVariable Long id) {
        return ResponseEntity.ok(choreService.getChoreById(id));
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<List<ChoreDTO>> getChoresByHousehold(
        @PathVariable Long householdId
    ) {
        return ResponseEntity.ok(
            choreService.getChoresByHousehold(householdId)
        );
    }

    // GET /api/chores/household/1?completed=false
    @GetMapping("/household/{householdId}/status")
    public ResponseEntity<List<ChoreDTO>> getChoresByHouseholdAndStatus(
        @PathVariable Long householdId,
        @RequestParam boolean completed
    ) {
        return ResponseEntity.ok(
            choreService.getChoresByHouseholdAndStatus(householdId, completed)
        );
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ChoreDTO>> getChoresByProfile(
        @PathVariable Long profileId
    ) {
        return ResponseEntity.ok(choreService.getChoresByProfile(profileId));
    }

    // GET /api/chores/profile/1/status?completed=false
    @GetMapping("/profile/{profileId}/status")
    public ResponseEntity<List<ChoreDTO>> getChoresByProfileAndStatus(
        @PathVariable Long profileId,
        @RequestParam boolean completed
    ) {
        return ResponseEntity.ok(
            choreService.getChoresByProfileAndStatus(profileId, completed)
        );
    }

    @PostMapping
    public ResponseEntity<ChoreDTO> createChore(
        @RequestBody Map<String, Object> payload
    ) {
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        Long householdId = Long.valueOf(payload.get("householdId").toString());

        Chore chore = new Chore();
        chore.setChoreName(payload.get("choreName").toString());
        chore.setChoreDescription(payload.get("choreDescription").toString());
        chore.setRepeatInterval(
            Integer.valueOf(payload.get("repeatInterval").toString())
        );
        chore.setIsCompleted(false);

        if (payload.containsKey("completeBy")) {
            chore.setCompleteBy(
                java.time.Instant.parse(payload.get("completeBy").toString())
            );
        }

        Chore created = choreService.createChore(profileId, householdId, chore);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            choreService.toDTO(created)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChoreDTO> updateChore(
        @PathVariable Long id,
        @RequestBody Chore chore
    ) {
        return ResponseEntity.ok(choreService.updateChore(id, chore));
    }

    // PATCH /api/chores/1/complete — mark a chore as complete
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ChoreDTO> markComplete(@PathVariable Long id) {
        return ResponseEntity.ok(choreService.markComplete(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChore(@PathVariable Long id) {
        choreService.deleteChore(id);
        return ResponseEntity.noContent().build();
    }
}
