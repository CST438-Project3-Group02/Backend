package com.roomie.service;

import com.roomie.entity.Activity;
import com.roomie.entity.ActivityComment;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ActivityCommentRepository;
import com.roomie.repository.ActivityRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ActivityCommentService {

    private final ActivityCommentRepository activityCommentRepository;
    private final ActivityRepository activityRepository;
    private final ProfileRepository profileRepository;

    public ActivityCommentService(
        ActivityCommentRepository activityCommentRepository,
        ActivityRepository activityRepository,
        ProfileRepository profileRepository
    ) {
        this.activityCommentRepository = activityCommentRepository;
        this.activityRepository = activityRepository;
        this.profileRepository = profileRepository;
    }

    public List<ActivityComment> getCommentsByActivity(Long activityId) {
        return activityCommentRepository.findByActivity_ActivityId(activityId);
    }

    public List<ActivityComment> getCommentsByProfile(Long profileId) {
        return activityCommentRepository.findByProfile_ProfileId(profileId);
    }

    public ActivityComment getCommentById(Long id) {
        return activityCommentRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ActivityComment", id)
            );
    }

    public ActivityComment addComment(
        Long profileId,
        Long activityId,
        String comment
    ) {
        Profile profile = profileRepository
            .findById(profileId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Profile", profileId)
            );
        Activity activity = activityRepository
            .findById(activityId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Activity", activityId)
            );

        ActivityComment activityComment = new ActivityComment(comment);
        activityComment.setProfile(profile);
        activityComment.setActivity(activity);
        return activityCommentRepository.save(activityComment);
    }

    public ActivityComment updateComment(Long id, String comment) {
        ActivityComment existing = activityCommentRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ActivityComment", id)
            );
        existing.setComment(comment);
        return activityCommentRepository.save(existing);
    }

    public void deleteComment(Long id) {
        if (!activityCommentRepository.existsById(id)) {
            throw new ResourceNotFoundException("ActivityComment", id);
        }
        activityCommentRepository.deleteById(id);
    }
}
