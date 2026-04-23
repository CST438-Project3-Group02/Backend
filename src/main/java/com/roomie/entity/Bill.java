package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Bill")
public class Bill {

    // TODO: set up fk constraints

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private String name;

    private Float cost;

    private Float splitPercentage; // May not need?

    private Integer interval; // encoded, will create enums

    private Instant dueDate;

    private Instant createdAt;

    public Bill() {}

    public Bill(
        String name,
        Float cost,
        Float splitPercentage,
        Integer interval,
        Instant dueDate,
        Instant createdAt
    ) {
        this.name = name;
        this.cost = cost;
        this.splitPercentage = splitPercentage;
        this.interval = interval;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    public Long getBillId() {
        return this.billId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCost() {
        return this.cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getSplitPercentage() {
        return this.splitPercentage;
    }

    public void setSplitPercentage(Float splitPercentage) {
        this.splitPercentage = splitPercentage;
    }

    public Integer getInterval() {
        return this.interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // TODO: tostring, equals, hashcode
}
