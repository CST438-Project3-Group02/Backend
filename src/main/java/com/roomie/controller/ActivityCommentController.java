package com.roomie.controller;

import com.roomie.entity.ActivityComment;
import com.roomie.service.ActivityCommentService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity-comments")
public class ActivityCommentController {

    private final ActivityCommentService activityCommentService;

    public ActivityCommentController(
        ActivityCommentService activityCommentService
    ) {
        this.activityCommentService = activityCommentService;
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<ActivityComment>> getCommentsByActivity(
        @PathVariable Long activityId
    ) {
        return ResponseEntity.ok(
            activityCommentService.getCommentsByActivity(activityId)
        );
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ActivityComment>> getCommentsByProfile(
        @PathVariable Long profileId
    ) {
        return ResponseEntity.ok(
            activityCommentService.getCommentsByProfile(profileId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityComment> getCommentById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(activityCommentService.getCommentById(id));
    }

    @PostMapping
    public ResponseEntity<ActivityComment> addComment(
        @RequestBody Map<String, Object> payload
    ) {
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        Long activityId = Long.valueOf(payload.get("activityId").toString());
        String comment = payload.get("comment").toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(
            activityCommentService.addComment(profileId, activityId, comment)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityComment> updateComment(
        @PathVariable Long id,
        @RequestBody Map<String, Object> payload
    ) {
        String comment = payload.get("comment").toString();
        return ResponseEntity.ok(
            activityCommentService.updateComment(id, comment)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        activityCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
