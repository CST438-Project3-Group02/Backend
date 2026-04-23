package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "grocery_item")
public class GroceryItem {

    // TODO: Set up fk constraints

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long GroceryItemId;

    private String itemName;

    private boolean isPurchased;

    private Instant createdAt;

    public GroceryItem() {}

    public GroceryItem(
        String itemName,
        boolean isPurchased,
        Instant createdAt
    ) {
        this.itemName = itemName;
        this.isPurchased = isPurchased;
        this.createdAt = createdAt;
    }

    public Long getGroceryItemId() {
        return GroceryItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return (
            "GroceryItem{" +
            "GroceryItemId=" +
            GroceryItemId +
            ", itemName='" +
            itemName +
            '\'' +
            ", isPurchased=" +
            isPurchased +
            ", createdAt=" +
            createdAt +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryItem that = (GroceryItem) o;
        return Objects.equals(GroceryItemId, that.GroceryItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(GroceryItemId);
    }
}
