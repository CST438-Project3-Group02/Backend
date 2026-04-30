package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "profile_household")
public class ProfileHousehold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileHouseholdId;

    private Integer privs;

    private Integer payInterval;

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    public ProfileHousehold() {

    }

    public ProfileHousehold(Long profileHouseholdId, Integer privs, Integer payInterval, Instant createdAt, Profile profile, Household household) {
        this.profileHouseholdId = profileHouseholdId;
        this.privs = privs;
        this.payInterval = payInterval;
        this.createdAt = createdAt;
        this.profile = profile;
        this.household = household;
    }

    public Long getProfileHouseholdId() {
        return profileHouseholdId;
    }

    public Integer getPrivs() {
        return privs;
    }

    public Integer getPayInterval() {
        return payInterval;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Profile getProfile() {
        return profile;
    }

    public Household getHousehold() {
        return household;
    }

    public void setPrivs(Integer privs) {
        this.privs = privs;
    }

    public void setPayInterval(Integer payInterval) {
        this.payInterval = payInterval;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    public Profile getProfile() {
        return profile;
    }

    public Household getHousehold() {
        return household;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    @Override
    public String toString() {
        return "ProfileHousehold{" +
                "profileHouseholdId=" + profileHouseholdId +
                ", privs=" + privs +
                ", payInterval=" + payInterval +
                ", createdAt=" + createdAt +
                ", profile=" + profile +
                ", household=" + household +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileHousehold that = (ProfileHousehold) o;
        return Objects.equals(profileHouseholdId, that.profileHouseholdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileHouseholdId);
    }
}
