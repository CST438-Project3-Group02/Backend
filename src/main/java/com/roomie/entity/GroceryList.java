package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "grocery_list")
public class GroceryList {

    // TODO: set up fk constraints

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groceryListId;

    private String listName;

    private Instant createdAt;

    public GroceryList() {}

    public GroceryList(String listName, Instant createdAt) {
        this.listName = listName;
        this.createdAt = createdAt;
    }

    public Long getGroceryListId() {
        return groceryListId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
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
            "GroceryList{" +
            "groceryListId=" +
            groceryListId +
            ", listName='" +
            listName +
            '\'' +
            ", createdAt=" +
            createdAt +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryList that = (GroceryList) o;
        return Objects.equals(groceryListId, that.groceryListId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groceryListId);
    }
}
