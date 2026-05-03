package com.roomie.repository;

import com.roomie.entity.Chore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoreRepository extends JpaRepository<Chore, Long> {
    List<Chore> findByHousehold_HouseholdId(Long householdId);
    List<Chore> findByProfile_ProfileId(Long profileId);
    List<Chore> findByHousehold_HouseholdIdAndIsCompleted(
        Long householdId,
        boolean isCompleted
    );
    List<Chore> findByProfile_ProfileIdAndIsCompleted(
        Long profileId,
        boolean isCompleted
    );
}
