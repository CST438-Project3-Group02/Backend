package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import tools.jackson.databind.ser.jdk.NumberSerializers.Base;

@Entity
@Table(name = "profile")
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    private String name;

    private String email;

    private Integer age;

    private String profilePicUrl;

    @Column(unique = true, nullable = false)
    private String oauth_id;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    ) //done
    private List<ProfileHousehold> profileHouseholds;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Bill> bills;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<GroceryItem> groceryItems;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<GroceryList> groceryLists;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ActivityComment> activityComments;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    ) // done
    private List<ActivityReaction> activityReactions;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    ) // done
    private List<Activity> activities;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    ) //done
    private List<Chore> chores;

    @OneToMany(
        mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Invite> invites;

    public Profile() {}

    public Profile(String name, String email, Integer age, String oauth_id) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.oauth_id = oauth_id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOauth_id() {
        return oauth_id;
    }

    public List<ProfileHousehold> getProfileHouseholds() {
        return profileHouseholds;
    }

    public void setProfileHouseholds(List<ProfileHousehold> profileHouseholds) {
        this.profileHouseholds = profileHouseholds;
    }

    public List<Chore> getChores() {
        return chores;
    }

    public void setChores(List<Chore> chores) {
        this.chores = chores;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<ActivityReaction> getActivityReactions() {
        return activityReactions;
    }

    public void setActivityReactions(List<ActivityReaction> activityReactions) {
        this.activityReactions = activityReactions;
    }

    public List<ActivityComment> getActivityComments() {
        return activityComments;
    }

    public void setActivityComments(List<ActivityComment> activityComments) {
        this.activityComments = activityComments;
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

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    @Override
    public String toString() {
        return (
            "Profile{" +
            "profile_id=" +
            profileId +
            ", name='" +
            name +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", age=" +
            age +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(profileId, profile.profileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }
}
