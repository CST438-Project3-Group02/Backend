package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "profile_household")
public class ProfileHousehold extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileHouseholdId;

    private Integer privs;

    private Integer payInterval;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    public ProfileHousehold() {}

    public ProfileHousehold(
        Long profileHouseholdId,
        Integer privs,
        Integer payInterval,
        Profile profile,
        Household household
    ) {
        this.profileHouseholdId = profileHouseholdId;
        this.privs = privs;
        this.payInterval = payInterval;
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

    public void setPrivs(Integer privs) {
        this.privs = privs;
    }

    public void setPayInterval(Integer payInterval) {
        this.payInterval = payInterval;
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
        return (
            "ProfileHousehold{" +
            "profileHouseholdId=" +
            profileHouseholdId +
            ", privs=" +
            privs +
            ", payInterval=" +
            payInterval +
            ", profile=" +
            profile +
            ", household=" +
            household +
            '}'
        );
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
