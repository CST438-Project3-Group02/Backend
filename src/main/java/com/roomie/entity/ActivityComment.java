package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "activity_comment")
public class ActivityComment {

    // TODO: fk constraints lmao
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ActivitiesCommentId;

    private String comment;

    private Instant createdAt;

    public ActivityComment() {}

    public ActivityComment(String comment, Instant createdAt) {
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getActivitiesCommentId() {
        return ActivitiesCommentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
            "ActivitiesComment{" +
            "ActivitiesCommentId=" +
            ActivitiesCommentId +
            ", comment='" +
            comment +
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
        ActivityComment that = (ActivityComment) o;
        return Objects.equals(ActivitiesCommentId, that.ActivitiesCommentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ActivitiesCommentId);
    }
}
