package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.List;

@Entity
@Table(name = "expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    private String description;

    private Double amount;

    private Double splitAmount;

    private Boolean paid;

    private Instant paidByDate;

    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bill> bills;

    public Expense() {

    }

    public Expense(String description, Double amount, Double splitAmount, Boolean paid, Instant paidByDate, Instant createdAt) {
        this.description = description;
        this.amount = amount;
        this.splitAmount = splitAmount;
        this.paid = paid;
        this.paidByDate = paidByDate;
        this.createdAt = createdAt;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getSplitAmount() {
        return splitAmount;
    }

    public void setSplitAmount(Double splitAmount) {
        this.splitAmount = splitAmount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Instant getPaidByDate() {
        return paidByDate;
    }

    public void setPaidByDate(Instant paidByDate) {
        this.paidByDate = paidByDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", splitAmount=" + splitAmount +
                ", paid=" + paid +
                ", paidByDate=" + paidByDate +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(expenseId, expense.expenseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expenseId);
    }
}
