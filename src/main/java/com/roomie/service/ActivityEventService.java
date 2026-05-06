package com.roomie.service;

import com.roomie.entity.Activity;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.enums.ActivityType;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.ActivityRepository;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityEventService {

    private final ActivityRepository activityRepository;
    private final ProfileRepository profileRepository;
    private final HouseholdRepository householdRepository;

    public ActivityEventService(
        ActivityRepository activityRepository,
        ProfileRepository profileRepository,
        HouseholdRepository householdRepository
    ) {
        this.activityRepository = activityRepository;
        this.profileRepository = profileRepository;
        this.householdRepository = householdRepository;
    }

    public void log(
        Long profileId,
        Long householdId,
        ActivityType type,
        boolean isCompleted
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

        Activity activity = new Activity(type.getValue(), isCompleted);
        activity.setProfile(profile);
        activity.setHousehold(household);
        activityRepository.save(activity);
    }
}
