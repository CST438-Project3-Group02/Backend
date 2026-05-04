package com.roomie.dto;

import com.roomie.entity.Invite;

import java.time.Instant;

public class InviteDTO {

    private Long inviteId;
    private String inviteCode;
    private Long profileId;
    private Long householdId;
    private Instant expirationDate;

    public InviteDTO(Invite invite) {
        this.inviteId = invite.getInviteId();
        this.inviteCode = invite.getInviteCode();
        this.expirationDate = invite.getExpirationDate();
        this.profileId = invite.getProfile() != null
                ? invite.getProfile().getProfileId()
                : null;
        this.householdId = invite.getHousehold() != null
                ? invite.getHousehold().getHouseholdId()
                : null;
    }

    public Long getInviteId() { return inviteId; }
    public String getInviteCode() { return inviteCode; }
    public Long getProfileId() { return profileId; }
    public Long getHouseholdId() { return householdId; }
    public Instant getExpirationDate() { return expirationDate; }
}