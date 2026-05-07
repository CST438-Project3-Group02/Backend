package com.roomie.dto;

import com.roomie.entity.*;
import java.util.List;

public class ProfileDTO {

    private Long profileId;
    private String name;
    private String email;
    private Integer age;
    private String profilePicUrl;
    private List<Household> households;
    private List<Bill> bills;
    private List<GroceryItem> groceryItems;
    private List<GroceryList> groceryLists;
    private List<ActivityComment> activityComments;
    private List<ActivityReaction> activityReactions;
    private List<Activity> activities;
    private List<Chore> chores;
    private List<Invite> invites;
    private ProfileHousehold memberships;

    public ProfileDTO(
        Long profileId,
        String name,
        String email,
        Integer age,
        String profilePicUrl
    ) {
        this.profileId = profileId;
        this.name = name;
        this.email = email;
        this.age = age;
        this.profilePicUrl = profilePicUrl;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public List<Household> getHouseholds() {
        return households;
    }

    public ProfileHousehold getMemberships() { return memberships; }

    public void setHouseholds(List<Household> households) {
        this.households = households;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<GroceryItem> getGroceryItems() {
        return groceryItems;
    }

    public void setGroceryItems(List<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
    }

    public List<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public void setGroceryLists(List<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Chore> getChores() {
        return chores;
    }

    public void setChores(List<Chore> chores) {
        this.chores = chores;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    public void setMemberships(ProfileHousehold memberships) {
        this.memberships = memberships;
    }
}
