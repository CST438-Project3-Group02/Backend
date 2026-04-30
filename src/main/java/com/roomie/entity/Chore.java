package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.List;

@Entity
@Table(name = "chore")
public class Chore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choreId;

    private String choreName;

    private String choreDescription;

    private Integer repeatInterval;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "household_id")
    private Household household;

    private Instant createdAt;

    private Instant completeBy;

    public Chore() {}

    public Chore(
        String choreName,
        String choreDescription,
        Integer repeatInterval,
        boolean isCompleted,
        Instant createdAt,
        Instant completeBy
    ) {
        this.choreName = choreName;
        this.choreDescription = choreDescription;
        this.repeatInterval = repeatInterval;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
        this.completeBy = completeBy;
    }

    public Long getChoreId() {
        return choreId;
    }

    public String getChoreName() {
        return choreName;
    }

    public void setChoreName(String choreName) {
        this.choreName = choreName;
    }

    public String getChoreDescription() {
        return choreDescription;
    }

    public void setChoreDescription(String choreDescription) {
        this.choreDescription = choreDescription;
    }

    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Integer repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean completed) {
        isCompleted = completed;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCompleteBy() {
        return completeBy;
    }

    public void setCompleteBy(Instant completeBy) {
        this.completeBy = completeBy;
    }

    @Override
    public String toString() {
        return (
            "Chores{" +
            "choreId=" +
            choreId +
            ", choreName='" +
            choreName +
            '\'' +
            ", choreDescription='" +
            choreDescription +
            '\'' +
            ", repeatInterval=" +
            repeatInterval +
            ", isCompleted=" +
            isCompleted +
            ", createdAt=" +
            createdAt +
            ", completeBy=" +
            completeBy +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chore chores = (Chore) o;
        return Objects.equals(choreId, chores.choreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(choreId);
    }
}
