package com.roomie.repository;

import com.roomie.entity.Household;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {
    List<Household> findByProfileHouseholds_Profile_OauthId(String oauthId);
}
