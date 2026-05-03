package com.roomie.service;

import com.roomie.entity.Activity;
import com.roomie.entity.ActivityReaction;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ActivityReactionRepository;
import com.roomie.repository.ActivityRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ActivityReactionService {

    private final ActivityReactionRepository activityReactionRepository;
    private final ActivityRepository activityRepository;
    private final ProfileRepository profileRepository;

    public ActivityReactionService(
        ActivityReactionRepository activityReactionRepository,
        ActivityRepository activityRepository,
        ProfileRepository profileRepository
    ) {
        this.activityReactionRepository = activityReactionRepository;
        this.activityRepository = activityRepository;
        this.profileRepository = profileRepository;
    }

    public List<ActivityReaction> getReactionsByActivity(Long activityId) {
        return activityReactionRepository.findByActivity_ActivityId(activityId);
    }

    public List<ActivityReaction> getReactionsByProfile(Long profileId) {
        return activityReactionRepository.findByProfile_ProfileId(profileId);
    }

    public ActivityReaction getReactionById(Long id) {
        return activityReactionRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ActivityReaction", id)
            );
    }

    public ActivityReaction addReaction(
        Long profileId,
        Long activityId,
        String reaction
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

        ActivityReaction activityReaction = new ActivityReaction(reaction);
        activityReaction.setProfile(profile);
        activityReaction.setActivity(activity);
        return activityReactionRepository.save(activityReaction);
    }

    public ActivityReaction updateReaction(Long id, String reaction) {
        ActivityReaction existing = activityReactionRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("ActivityReaction", id)
            );
        existing.setReaction(reaction);
        return activityReactionRepository.save(existing);
    }

    public void deleteReaction(Long id) {
        if (!activityReactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("ActivityReaction", id);
        }
        activityReactionRepository.deleteById(id);
    }
}
