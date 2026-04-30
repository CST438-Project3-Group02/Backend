package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "activity")
public class Activity {

    // TODO: set up FK relationships
    // TODO: encode w/ enums

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    private Integer activityType;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private Instant createdAt;

    private boolean isCompleted;

    public Activity() {}

    public Activity(
        Integer activityType,
        Instant createdAt,
        boolean isCompleted
    ) {
        this.activityType = activityType;
        this.createdAt = createdAt;
        this.isCompleted = isCompleted;
    }

    public Long getActivityId() {
        return activityId;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return (
            "Activities{" +
            "activityId=" +
            activityId +
            ", activityType=" +
            activityType +
            ", createdAt=" +
            createdAt +
            ", isCompleted=" +
            isCompleted +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity that = (Activity) o;
        return Objects.equals(activityId, that.activityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId);
    }
}
