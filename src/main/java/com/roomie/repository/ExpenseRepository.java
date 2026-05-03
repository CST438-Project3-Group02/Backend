package com.roomie.repository;

import com.roomie.entity.Expense;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByHousehold_HouseholdId(Long householdId);
    List<Expense> findByHousehold_HouseholdIdAndPaid(
        Long householdId,
        Boolean paid
    );

    @Query(
        "SELECT e FROM Expense e LEFT JOIN FETCH e.bills WHERE e.expenseId = :id"
    )
    Optional<Expense> findExpenseWithBills(@Param("id") Long id);

    @Query(
        "SELECT e FROM Expense e LEFT JOIN FETCH e.bills WHERE e.household.householdId = :householdId"
    )
    List<Expense> findHouseholdExpensesWithBills(
        @Param("householdId") Long householdId
    );
}
