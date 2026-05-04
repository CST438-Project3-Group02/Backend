package com.roomie.service;

import com.roomie.dto.InviteDTO;
import com.roomie.entity.Household;
import com.roomie.entity.Invite;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.InviteRepository;
import com.roomie.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class InviteService {
    private final InviteRepository inviteRepository;
    private final ProfileRepository profileRepository;
    private final HouseholdRepository householdRepository;

    public InviteDTO toDTO(Invite invite) {
        return new InviteDTO(invite);
    }

    public InviteService(
            InviteRepository inviteRepository,
            ProfileRepository profileRepository,
            HouseholdRepository householdRepository
    ) {
        this.inviteRepository = inviteRepository;
        this.profileRepository = profileRepository;
        this.householdRepository = householdRepository;
    }

    public InviteDTO createInvite(Long profileId, Long householdId) {
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

        Invite invite = new Invite();
        invite.setProfile(profile);
        invite.setHousehold(household);
        invite.setInviteCode(UUID.randomUUID().toString().replace("-", ""));

        return new InviteDTO(inviteRepository.save(invite));
    }
}
