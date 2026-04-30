package com.roomie.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.time.Instant;


@Entity
@Table(name = "bill")
public class Bill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private String description;

    private Double amount;

    private Boolean paid;

    private Instant payByDate;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    public Bill() {}

    public Bill(
        String description,
        Double amount,
        Boolean paid,
        Instant payByDate,
        Profile profile
    ) {
        this.description = description;
        this.amount = amount;
        this.paid = paid;
        this.payByDate = payByDate;
        this.profile = profile;
    }

    public Long getBillId() {
        return billId;
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

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Instant getPayByDate() {
        return payByDate;
    }

    public void setPayByDate(Instant payByDate) {
        this.payByDate = payByDate;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    @Override
    public String toString() {
        return (
            "Bill{" +
            "billId=" +
            billId +
            ", description='" +
            description +
            '\'' +
            ", amount=" +
            amount +
            ", paid=" +
            paid +
            ", payByDate=" +
            payByDate +
            ", profile=" +
            profile +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(billId, bill.billId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId);
    }
}
