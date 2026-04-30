package com.roomie.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "activity_reaction")
public class ActivityReaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activitiesReactionId;

    private String reaction;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    public ActivityReaction() {}

    public ActivityReaction(String reaction) {
        this.reaction = reaction;
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
            "ActivitiesReaction{" +
            "activitiesReactionId=" +
            activitiesReactionId +
            ", reaction='" +
            reaction +
            '\'' +
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
