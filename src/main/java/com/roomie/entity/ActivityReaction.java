package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "activity_reaction")
public class ActivityReaction {

    // TODO: set up fk constraints

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activitiesReactionId;

    private String reaction;

    private Instant createdAt;

    public ActivityReaction() {}

    public ActivityReaction(String reaction, Instant createdAt) {
        this.reaction = reaction;
        this.createdAt = createdAt;
    }

    public Long getActivitiesReactionId() {
        return activitiesReactionId;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return (
            "ActivitiesReaction{" +
            "activitiesReactionId=" +
            activitiesReactionId +
            ", reaction='" +
            reaction +
            '\'' +
            ", createdAt=" +
            createdAt +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityReaction that = (ActivityReaction) o;
        return Objects.equals(that.activitiesReactionId, activitiesReactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(activitiesReactionId);
    }
}
