package com.roomie.dto;

import com.roomie.entity.*;
import java.util.List;

public class HouseholdDTO {

    private Long householdId;
    private String householdName;
    private Float rentCost;
    private Integer numOfBedrooms;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private List<ProfileDTO> profiles;
    private List<Chore> chores;
    private List<GroceryList> groceryLists;
    private List<Expense> expenses;
    private List<Invite> invites;

    public HouseholdDTO(
        Long householdId,
        String householdName,
        Float rentCost,
        Integer numOfBedrooms,
        String address,
        String city,
        String state,
        String zipCode,
        String country
    ) {
        this.householdId = householdId;
        this.householdName = householdName;
        this.rentCost = rentCost;
        this.numOfBedrooms = numOfBedrooms;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public Float getRentCost() {
        return rentCost;
    }

    public Integer getNumOfBedrooms() {
        return numOfBedrooms;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public List<ProfileDTO> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileDTO> profiles) {
        this.profiles = profiles;
    }

    public List<Chore> getChores() {
        return chores;
    }

    public void setChores(List<Chore> chores) {
        this.chores = chores;
    }

    public List<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public void setGroceryLists(List<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }
}
