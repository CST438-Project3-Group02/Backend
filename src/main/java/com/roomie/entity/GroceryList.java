package com.roomie.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "grocery_list")
public class GroceryList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groceryListId;

    private String listName;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @OneToMany(
        mappedBy = "groceryList",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<GroceryItem> groceryItems;

    public GroceryList() {}

    public GroceryList(String listName) {
        this.listName = listName;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }

    public List<GroceryItem> getGroceryItems() {
        return groceryItems;
    }

    public void setGroceryItems(List<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
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
