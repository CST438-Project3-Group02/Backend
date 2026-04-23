package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "profile")
public class Profile {

    // TODO: set up userId foreign key relationship w/ oauth table
    // TODO: set up householdId foreign key relationship
    // TODO: set up all fk relationships lmao
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    private String name;

    private String email;

    private Integer age;

    @Column(unique = true, nullable = false)
    private String oauth_id;

    private Instant createdAt;

    public Profile() {}

    public Profile(
        String name,
        String email,
        Integer age,
        String oauth_id,
        Instant createdAt
    ) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.oauth_id = oauth_id;
        this.createdAt = createdAt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOauth_id() {
        return oauth_id;
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
            ", oauth_id='" +
            oauth_id +
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
