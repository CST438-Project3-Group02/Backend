package com.roomie.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "grocery_item")
public class GroceryItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long GroceryItemId;

    private String itemName;

    private boolean isPurchased;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "grocery_list_id", nullable = false)
    private GroceryList groceryList;

    public GroceryItem() {}

    public GroceryItem(String itemName, boolean isPurchased) {
        this.itemName = itemName;
        this.isPurchased = isPurchased;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public GroceryList getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(GroceryList groceryList) {
        this.groceryList = groceryList;
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
