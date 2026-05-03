package com.roomie.dto;

import com.roomie.entity.Bill;
import java.time.Instant;
import java.util.List;

public class ExpenseDTO {

    private Long expenseId;
    private String description;
    private Double amount;
    private Double splitAmount;
    private Boolean paid;
    private Instant paidByDate;
    private Instant createdAt;
    private Long householdId;
    private String householdName;

    // null unless explicitly requested
    private List<Bill> bills;

    public ExpenseDTO(
        Long expenseId,
        String description,
        Double amount,
        Double splitAmount,
        Boolean paid,
        Instant paidByDate,
        Instant createdAt,
        Long householdId,
        String householdName
    ) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.splitAmount = splitAmount;
        this.paid = paid;
        this.paidByDate = paidByDate;
        this.createdAt = createdAt;
        this.householdId = householdId;
        this.householdName = householdName;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getSplitAmount() {
        return splitAmount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public Instant getPaidByDate() {
        return paidByDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
