package com.roomie.repository;

import com.roomie.entity.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByOauthId(String oauthId);
    boolean existsByOauthId(String oauthId);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.profileHouseholds ph LEFT JOIN FETCH ph.household WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithHouseholds(@Param("id") Long id);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.chores WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithChores(@Param("id") Long id);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.bills WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithBills(@Param("id") Long id);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.groceryLists WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithGroceryLists(@Param("id") Long id);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.groceryItems WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithGroceryItems(@Param("id") Long id);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.activities WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithActivities(@Param("id") Long id);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.activityComments WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithActivityComments(@Param("id") Long id);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.activityReactions WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithActivityReactions(@Param("id") Long id);

    @Query(
        "SELECT p FROM Profile p LEFT JOIN FETCH p.invites WHERE p.profileId = :id"
    )
    Optional<Profile> findProfileWithInvites(@Param("id") Long id);
}
