package com.roomie.repository;

import com.roomie.entity.Household;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, Long> {
    @Query(
        "SELECT h FROM Household h LEFT JOIN FETCH h.profileHouseholds ph LEFT JOIN FETCH ph.profile WHERE h.householdId = :id"
    )
    Optional<Household> findHouseholdWithProfiles(@Param("id") Long id);

    @Query(
        "SELECT h FROM Household h LEFT JOIN FETCH h.chores WHERE h.householdId = :id"
    )
    Optional<Household> findHouseholdWithChores(@Param("id") Long id);

    @Query(
        "SELECT h FROM Household h LEFT JOIN FETCH h.groceryLists WHERE h.householdId = :id"
    )
    Optional<Household> findHouseholdWithGroceryLists(@Param("id") Long id);

    @Query(
        "SELECT h FROM Household h LEFT JOIN FETCH h.expenses WHERE h.householdId = :id"
    )
    Optional<Household> findHouseholdWithExpenses(@Param("id") Long id);

    @Query(
        "SELECT h FROM Household h LEFT JOIN FETCH h.invites WHERE h.householdId = :id"
    )
    Optional<Household> findHouseholdWithInvites(@Param("id") Long id);
}
