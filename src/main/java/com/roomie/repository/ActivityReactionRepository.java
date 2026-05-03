package com.roomie.repository;

import com.roomie.entity.ActivityReaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityReactionRepository
    extends JpaRepository<ActivityReaction, Long>
{
    List<ActivityReaction> findByActivity_ActivityId(Long activityId);
    List<ActivityReaction> findByProfile_ProfileId(Long profileId);
}
