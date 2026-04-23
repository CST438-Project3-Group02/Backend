package com.roomie.repository;

import com.roomie.entity.ActivityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityCommentRepository
    extends JpaRepository<ActivityComment, Long> {}
