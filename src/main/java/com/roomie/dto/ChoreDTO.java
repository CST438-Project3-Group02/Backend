package com.roomie.dto;

import com.roomie.entity.Chore;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import java.time.Instant;

public class ChoreDTO {

    private Long choreId;
    private String choreName;
    private String choreDescription;
    private Integer repeatInterval;
    private boolean isCompleted;
    private Instant completeBy;
    private Instant createdAt;
    private Long profileId;
    private String profileName;
    private Long householdId;
    private String householdName;

    public ChoreDTO(Chore chore) {
        this.choreId = chore.getChoreId();
        this.choreName = chore.getChoreName();
        this.choreDescription = chore.getChoreDescription();
        this.repeatInterval = chore.getRepeatInterval();
        this.isCompleted = chore.isCompleted();
        this.completeBy = chore.getCompleteBy();
        this.createdAt = chore.getCreatedAt();
        this.profileId =
            chore.getProfile() != null
                ? chore.getProfile().getProfileId()
                : null;
        this.profileName =
            chore.getProfile() != null ? chore.getProfile().getName() : null;
        this.householdId =
            chore.getHousehold() != null
                ? chore.getHousehold().getHouseholdId()
                : null;
        this.householdName =
            chore.getHousehold() != null
                ? chore.getHousehold().getHouseholdName()
                : null;
    }

    public Long getChoreId() {
        return choreId;
    }

    public String getChoreName() {
        return choreName;
    }

    public String getChoreDescription() {
        return choreDescription;
    }

    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public Instant getCompleteBy() {
        return completeBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public String getHouseholdName() {
        return householdName;
    }
}
