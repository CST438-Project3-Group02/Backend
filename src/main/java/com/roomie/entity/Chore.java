package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "chore")
public class Chore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choreId;

    // TODO: set up profile_id foreign key relationship

    // TODO: set up household_id foreign key relationship

    private String choreName;

    private String choreDescription;

    private Integer repeatInterval;

    private boolean isCompleted;

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
