package com.roomie.repository;

import com.roomie.entity.Activity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByHousehold_HouseholdId(Long householdId);
    List<Activity> findByHousehold_HouseholdIdOrderByCreatedAtDesc(Long householdId);
    List<Activity> findByProfile_ProfileId(Long profileId);

    @Query(
        "SELECT a FROM Activity a LEFT JOIN FETCH a.activityComments WHERE a.activityId = :id"
    )
    Optional<Activity> findActivityWithComments(@Param("id") Long id);

    @Query(
        "SELECT a FROM Activity a LEFT JOIN FETCH a.activityReactions WHERE a.activityId = :id"
    )
    Optional<Activity> findActivityWithReactions(@Param("id") Long id);
}
