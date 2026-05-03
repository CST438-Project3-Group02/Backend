package com.roomie.controller;

import com.roomie.entity.ActivityReaction;
import com.roomie.service.ActivityReactionService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity-reactions")
public class ActivityReactionController {

    private final ActivityReactionService activityReactionService;

    public ActivityReactionController(
        ActivityReactionService activityReactionService
    ) {
        this.activityReactionService = activityReactionService;
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<ActivityReaction>> getReactionsByActivity(
        @PathVariable Long activityId
    ) {
        return ResponseEntity.ok(
            activityReactionService.getReactionsByActivity(activityId)
        );
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ActivityReaction>> getReactionsByProfile(
        @PathVariable Long profileId
    ) {
        return ResponseEntity.ok(
            activityReactionService.getReactionsByProfile(profileId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityReaction> getReactionById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(activityReactionService.getReactionById(id));
    }

    @PostMapping
    public ResponseEntity<ActivityReaction> addReaction(
        @RequestBody Map<String, Object> payload
    ) {
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        Long activityId = Long.valueOf(payload.get("activityId").toString());
        String reaction = payload.get("reaction").toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(
            activityReactionService.addReaction(profileId, activityId, reaction)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityReaction> updateReaction(
        @PathVariable Long id,
        @RequestBody Map<String, Object> payload
    ) {
        String reaction = payload.get("reaction").toString();
        return ResponseEntity.ok(
            activityReactionService.updateReaction(id, reaction)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        activityReactionService.deleteReaction(id);
        return ResponseEntity.noContent().build();
    }
}
