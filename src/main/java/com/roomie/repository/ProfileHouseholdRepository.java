package com.roomie.repository;

import com.roomie.entity.ProfileHousehold;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileHouseholdRepository
    extends JpaRepository<ProfileHousehold, Long>
{
    List<ProfileHousehold> findByProfile_ProfileId(Long profileId);
    List<ProfileHousehold> findByHousehold_HouseholdId(Long householdId);
}
