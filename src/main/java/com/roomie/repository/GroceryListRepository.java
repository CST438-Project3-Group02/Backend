package com.roomie.repository;

import com.roomie.entity.GroceryList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryListRepository
    extends JpaRepository<GroceryList, Long>
{
    List<GroceryList> findByHousehold_HouseholdId(Long householdId);
    List<GroceryList> findByProfile_ProfileId(Long profileId);

    @Query(
        "SELECT g FROM GroceryList g LEFT JOIN FETCH g.groceryItems WHERE g.groceryListId = :id"
    )
    Optional<GroceryList> findGroceryListWithItems(@Param("id") Long id);

    @Query(
        "SELECT g FROM GroceryList g LEFT JOIN FETCH g.groceryItems WHERE g.household.householdId = :householdId"
    )
    List<GroceryList> findHouseholdListsWithItems(
        @Param("householdId") Long householdId
    );
}
