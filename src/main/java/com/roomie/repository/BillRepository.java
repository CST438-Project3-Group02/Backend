package com.roomie.repository;

import com.roomie.entity.Bill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByProfile_ProfileId(Long profileId);
    List<Bill> findByExpense_ExpenseId(Long expenseId);
    List<Bill> findByProfile_ProfileIdAndPaid(Long profileId, Boolean paid);
    List<Bill> findByExpense_ExpenseId_AndPaid(Long expenseId, Boolean paid);
}
