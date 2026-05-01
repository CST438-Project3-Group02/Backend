package com.roomie.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "household")
public class Household extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long householdId;

    private Float rentCost;

    private Integer numOfBedrooms;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    private String householdName;

    @OneToMany(
        mappedBy = "household",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ProfileHousehold> profileHouseholds;

    @OneToMany(
        mappedBy = "household",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Chore> chores;

    @OneToMany(
        mappedBy = "household",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<GroceryList> groceryLists;

    @OneToMany(
        mappedBy = "household",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Expense> expenses;

    @OneToMany(
        mappedBy = "household",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Invite> invites;

    public Household() {}

    public Household(
        Float rentCost,
        Integer numOfBedrooms,
        String address,
        String city,
        String state,
        String zipCode,
        String country,
        String householdName
    ) {
        this.rentCost = rentCost;
        this.numOfBedrooms = numOfBedrooms;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.householdName = householdName;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public Float getRentCost() {
        return rentCost;
    }

    public void setRentCost(Float rentCost) {
        this.rentCost = rentCost;
    }

    public Integer getNumOfBedrooms() {
        return numOfBedrooms;
    }

    public void setNumOfBedrooms(Integer numOfBedrooms) {
        this.numOfBedrooms = numOfBedrooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<ProfileHousehold> getProfileHouseholds() {
        return profileHouseholds;
    }

    public void setProfileHouseholds(List<ProfileHousehold> profileHouseholds) {
        this.profileHouseholds = profileHouseholds;
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

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    @Override
    public String toString() {
        return (
            "Household{" +
            "householdId=" +
            householdId +
            ", rentCost=" +
            rentCost +
            ", numOfBedrooms=" +
            numOfBedrooms +
            ", address='" +
            address +
            '\'' +
            ", city='" +
            city +
            '\'' +
            ", state='" +
            state +
            '\'' +
            ", zipCode='" +
            zipCode +
            '\'' +
            ", country='" +
            country +
            '\'' +
            ", householdName='" +
            householdName +
            '\'' +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Household household = (Household) o;
        return Objects.equals(householdId, household.householdId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(householdId);
    }
}
