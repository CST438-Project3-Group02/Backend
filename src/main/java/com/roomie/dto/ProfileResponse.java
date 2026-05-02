package com.roomie.dto;

import com.roomie.entity.Profile;

public class ProfileResponse {

    private Long profileId;
    private String name;
    private String email;
    private Integer age;
    private String profilePicUrl;
    private String oauthId;

    public ProfileResponse(Profile profile) {
        this.profileId = profile.getProfileId();
        this.name = profile.getName();
        this.email = profile.getEmail();
        this.age = profile.getAge();
        this.profilePicUrl = profile.getProfilePicUrl();
        this.oauthId = profile.getOauthId();
    }

    public Long getProfileId() { return profileId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Integer getAge() { return age; }
    public String getProfilePicUrl() { return profilePicUrl; }
    public String getOauthId() { return oauthId; }
}
