package com.roomie.controller;

import com.roomie.dto.ActivityDTO;
import com.roomie.entity.Activity;
import com.roomie.service.ActivityService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ActivityDTO> getActivityWithComments(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(activityService.getActivityWithComments(id));
    }

    @GetMapping("/{id}/reactions")
    public ResponseEntity<ActivityDTO> getActivityWithReactions(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(activityService.getActivityWithReactions(id));
    }

    @GetMapping("/{id}/full")
    public ResponseEntity<ActivityDTO> getActivityWithAll(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(activityService.getActivityWithAll(id));
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<List<ActivityDTO>> getActivityFeedByHousehold(
        @PathVariable Long householdId
    ) {
        return ResponseEntity.ok(
            activityService.getActivityFeedByHousehold(householdId)
        );
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByProfile(
        @PathVariable Long profileId
    ) {
        return ResponseEntity.ok(
            activityService.getActivitiesByProfile(profileId)
        );
    }

    @PostMapping
    public ResponseEntity<ActivityDTO> createActivity(
        @RequestBody Map<String, Object> payload
    ) {
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        Long householdId = Long.valueOf(payload.get("householdId").toString());
        Integer activityType = Integer.valueOf(
            payload.get("activityType").toString()
        );
        Activity activity = activityService.createActivity(
            profileId,
            householdId,
            activityType
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
            activityService.toDTO(activity)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDTO> updateActivity(
        @PathVariable Long id,
        @RequestBody Activity activity
    ) {
        return ResponseEntity.ok(activityService.updateActivity(id, activity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
