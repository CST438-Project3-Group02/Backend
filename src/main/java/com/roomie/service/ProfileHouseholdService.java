package com.roomie.service;

import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
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

    public ProfileHouseholdService(
        ProfileHouseholdRepository profileHouseholdRepository,
        ProfileRepository profileRepository,
        HouseholdRepository householdRepository
    ) {
        this.profileHouseholdRepository = profileHouseholdRepository;
        this.profileRepository = profileRepository;
        this.householdRepository = householdRepository;
    }

    public ProfileHousehold getMembershipById(Long id) {
        return profileHouseholdRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Membership", id));
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
        if (!profileHouseholdRepository.existsById(id)) {
            throw new ResourceNotFoundException("Membership", id);
        }
        profileHouseholdRepository.deleteById(id);
    }
}
