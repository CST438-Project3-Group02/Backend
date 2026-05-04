package com.roomie.repository;

import com.roomie.entity.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findByInviteCode(String inviteCode);
}
