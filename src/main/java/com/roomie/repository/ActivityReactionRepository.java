package com.roomie.repository;

import com.roomie.entity.ActivityReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityReactionRepository
    extends JpaRepository<ActivityReaction, Long> {}
