package com.roomie.service;

import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.enums.ActivityType;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileHouseholdRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProfileHouseholdService {

    private final ProfileHouseholdRepository profileHouseholdRepository;
    private final ProfileRepository profileRepository;
    private final HouseholdRepository householdRepository;
    private final ActivityEventService activityEventService;

    public ProfileHouseholdService(
        ProfileHouseholdRepository profileHouseholdRepository,
        ProfileRepository profileRepository,
        HouseholdRepository householdRepository,
        ActivityEventService activityEventService
    ) {
        this.profileHouseholdRepository = profileHouseholdRepository;
        this.profileRepository = profileRepository;
        this.householdRepository = householdRepository;
        this.activityEventService = activityEventService;
    }

    public ProfileHousehold getMembershipById(Long id) {
        return profileHouseholdRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Membership", id));
    }

    public ProfileHousehold getMembershipByProfileAndHousehold(
        Long profileId,
        Long householdId
    ) {
        return profileHouseholdRepository
            .findByProfileProfileIdAndHouseholdHouseholdId(
                profileId,
                householdId
            )
            .orElseThrow(() ->
                new ResourceNotFoundException("Membership", profileId)
            );
    }

    public ProfileHousehold addMember(
        Long profileId,
        Long householdId,
        Integer privs,
        Integer payInterval
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

        ProfileHousehold membership = new ProfileHousehold();
        membership.setProfile(profile);
        membership.setHousehold(household);
        membership.setPrivs(privs);
        membership.setPayInterval(payInterval);
        activityEventService.log(
            profileId,
            householdId,
            ActivityType.MEMBER_JOINED,
            false
        );

        return profileHouseholdRepository.save(membership);
    }

    public ProfileHousehold updateMember(Long id, ProfileHousehold updated) {
        ProfileHousehold existing = profileHouseholdRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Membership", id));

        existing.setPrivs(updated.getPrivs());
        existing.setPayInterval(updated.getPayInterval());

        return profileHouseholdRepository.save(existing);
    }

    public void removeMember(Long id) {
        ProfileHousehold membership = profileHouseholdRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Membership", id));
        if (!profileHouseholdRepository.existsById(id)) {
            throw new ResourceNotFoundException("Membership", id);
        }
        Long profileId = membership.getProfile().getProfileId();
        Long householdId = membership.getHousehold().getHouseholdId();
        profileHouseholdRepository.deleteById(id);
        activityEventService.log(
            profileId,
            householdId,
            ActivityType.MEMBER_LEFT,
            false
        );
    }
}
