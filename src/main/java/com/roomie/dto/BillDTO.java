package com.roomie.dto;

import java.time.Instant;

public class BillDTO {

    private Long billId;
    private String description;
    private Double amount;
    private Boolean paid;
    private Instant payByDate;
    private Instant createdAt;
    private Long profileId;
    private String profileName;
    private Long expenseId;

    public BillDTO(
        Long billId,
        String description,
        Double amount,
        Boolean paid,
        Instant payByDate,
        Instant createdAt,
        Long profileId,
        String profileName,
        Long expenseId
    ) {
        this.billId = billId;
        this.description = description;
        this.amount = amount;
        this.paid = paid;
        this.payByDate = payByDate;
        this.createdAt = createdAt;
        this.profileId = profileId;
        this.profileName = profileName;
        this.expenseId = expenseId;
    }

    public Long getBillId() {
        return billId;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public Boolean getPaid() {
        return paid;
    }

    public Instant getPayByDate() {
        return payByDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public Long getExpenseId() {
        return expenseId;
    }
}
