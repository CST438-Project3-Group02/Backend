package com.roomie.dto;

import com.roomie.entity.Household;

public class HouseholdResponse {
    private Long householdId;
    private String householdName;
    private Float rentCost;
    private Integer numOfBedrooms;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public HouseholdResponse(Household household) {
        this.householdId = household.getHouseholdId();
        this.householdName = household.getHouseholdName();
        this.rentCost = household.getRentCost();
        this.numOfBedrooms = household.getNumOfBedrooms();
        this.address = household.getAddress();
        this.city = household.getCity();
        this.state = household.getState();
        this.zipCode = household.getZipCode();
        this.country = household.getCountry();
    }

    public Long getHouseholdId() { return householdId; }
    public String getHouseholdName() { return householdName; }
    public Float getRentCost() { return rentCost; }
    public Integer getNumOfBedrooms() { return numOfBedrooms; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
}