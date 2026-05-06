package com.roomie.dto;

import com.roomie.entity.ActivityComment;
import com.roomie.entity.ActivityReaction;
import com.roomie.entity.Profile;
import java.util.List;

public class ActivityDTO {

    private Long activityId;
    private Integer activityType;
    private boolean isCompleted;
    private Profile profile;
    private String postComment;
    private String picUrl;

    private List<ActivityComment> activityComments;
    private List<ActivityReaction> activityReactions;

    public ActivityDTO(
        Long activityId,
        Integer activityType,
        boolean isCompleted,
        Profile profile,
        String postComment,
        String picUrl
    ) {
        this.activityId = activityId;
        this.activityType = activityType;
        this.isCompleted = isCompleted;
        this.profile = profile;
        this.postComment = postComment;
        this.picUrl = picUrl;
    }

    public Long getActivityId() {
        return activityId;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getPostComment() {
        return postComment;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public List<ActivityComment> getActivityComments() {
        return activityComments;
    }

    public void setActivityComments(List<ActivityComment> activityComments) {
        this.activityComments = activityComments;
    }

    public List<ActivityReaction> getActivityReactions() {
        return activityReactions;
    }

    public void setActivityReactions(List<ActivityReaction> activityReactions) {
        this.activityReactions = activityReactions;
    }
}
