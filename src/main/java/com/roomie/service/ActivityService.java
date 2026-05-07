package com.roomie.service;

import com.roomie.dto.ActivityDTO;
import com.roomie.entity.Activity;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ActivityRepository;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ProfileRepository profileRepository;
    private final HouseholdRepository householdRepository;

    public ActivityService(
        ActivityRepository activityRepository,
        ProfileRepository profileRepository,
        HouseholdRepository householdRepository
    ) {
        this.activityRepository = activityRepository;
        this.profileRepository = profileRepository;
        this.householdRepository = householdRepository;
    }

    public ActivityDTO toDTO(Activity activity) {
        return new ActivityDTO(
            activity.getActivityId(),
            activity.getActivityType(),
            activity.isCompleted(),
            activity.getProfile(),
            activity.getPostComment(),
            activity.getPicUrl(),
            activity.getCreatedAt()
        );
    }

    public List<ActivityDTO> getAllActivities() {
        return activityRepository
            .findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public ActivityDTO getActivityById(Long id) {
        Activity activity = activityRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity", id));
        return toDTO(activity);
    }

    public List<ActivityDTO> getActivityFeedByHousehold(Long householdId) {
        return activityRepository
            .findByHousehold_HouseholdIdOrderByCreatedAtDesc(householdId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<ActivityDTO> getActivitiesByProfile(Long profileId) {
        return activityRepository
            .findByProfile_ProfileId(profileId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public ActivityDTO getActivityWithComments(Long id) {
        Activity activity = activityRepository
            .findActivityWithComments(id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity", id));
        ActivityDTO dto = toDTO(activity);
        dto.setActivityComments(activity.getActivityComments());
        return dto;
    }

    public ActivityDTO getActivityWithReactions(Long id) {
        Activity activity = activityRepository
            .findActivityWithReactions(id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity", id));
        ActivityDTO dto = toDTO(activity);
        dto.setActivityReactions(activity.getActivityReactions());
        return dto;
    }

    public ActivityDTO getActivityWithAll(Long id) {
        // two separate queries to avoid MultipleBagFetchException
        Activity activityWithComments = activityRepository
            .findActivityWithComments(id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity", id));

        Activity activityWithReactions = activityRepository
            .findActivityWithReactions(id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity", id));

        ActivityDTO dto = toDTO(activityWithComments);
        dto.setActivityComments(activityWithComments.getActivityComments());
        dto.setActivityReactions(activityWithReactions.getActivityReactions());
        return dto;
    }

    public Activity createActivity(
        Long profileId,
        Long householdId,
        Integer activityType,
        String postComment,
        String picUrl
    ) {
        Profile profile = profileRepository
            .findById(profileId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Profile", profileId)
            );
        Household household = householdRepository
            .findById(householdId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Household", householdId)
            );

        Activity activity = new Activity(activityType, false);
        activity.setProfile(profile);
        activity.setHousehold(household);
        activity.setPostComment(postComment);
        activity.setPicUrl(picUrl);
        return activityRepository.save(activity);
    }

    public ActivityDTO updateActivity(Long id, Activity updatedActivity) {
        Activity existing = activityRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Activity", id));
        existing.setActivityType(updatedActivity.getActivityType());
        existing.setCompleted(updatedActivity.isCompleted());
        return toDTO(activityRepository.save(existing));
    }

    public void deleteActivity(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Activity", id);
        }
        activityRepository.deleteById(id);
    }
}
