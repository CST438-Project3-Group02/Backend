package com.roomie.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "activity_comment")
public class ActivityComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ActivitiesCommentId;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    public ActivityComment() {}

    public ActivityComment(String comment) {
        this.comment = comment;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
