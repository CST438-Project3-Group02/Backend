package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "invite")
public class Invite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inviteId;

    private String inviteCode;

    @ManyToOne
    @JoinColumn(name = "referrer_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "household_id")
    private Household household;

    private Instant expirationDate;

    public Long getInviteId() {
        return inviteId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return (
            "Invite{" +
            "inviteId=" +
            inviteId +
            ", inviteCode='" +
            inviteCode +
            '\'' +
            ", profile=" +
            profile +
            ", household=" +
            household +
            ", expirationDate=" +
            expirationDate +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invite invite = (Invite) o;
        return Objects.equals(inviteId, invite.inviteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inviteId);
    }
}
