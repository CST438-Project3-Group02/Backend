package com.roomie.dto;

import com.roomie.entity.GroceryItem;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import java.util.List;

public class GroceryListDTO {

    private Long groceryListId;
    private String listName;
    private Long profileId;
    private String profileName;
    private Long householdId;
    private String householdName;

    // null unless explicitly requested
    private List<GroceryItem> groceryItems;

    public GroceryListDTO(
        Long groceryListId,
        String listName,
        Profile profile,
        Household household
    ) {
        this.groceryListId = groceryListId;
        this.listName = listName;
        this.profileId = profile != null ? profile.getProfileId() : null;
        this.profileName = profile != null ? profile.getName() : null;
        this.householdId =
            household != null ? household.getHouseholdId() : null;
        this.householdName =
            household != null ? household.getHouseholdName() : null;
    }

    public Long getGroceryListId() {
        return groceryListId;
    }

    public String getListName() {
        return listName;
    }

    public Long getProfileId() {
        return profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public List<GroceryItem> getGroceryItems() {
        return groceryItems;
    }

    public void setGroceryItems(List<GroceryItem> groceryItems) {
        this.groceryItems = groceryItems;
    }
}
