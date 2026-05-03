package com.roomie.repository;

import com.roomie.entity.ActivityComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityCommentRepository
    extends JpaRepository<ActivityComment, Long>
{
    List<ActivityComment> findByActivity_ActivityId(Long activityId);
    List<ActivityComment> findByProfile_ProfileId(Long profileId);
}
