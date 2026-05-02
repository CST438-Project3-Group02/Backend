package com.roomie.service;

import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.HouseholdRepository;
import java.util.List;
import java.util.Optional;

import com.roomie.repository.ProfileHouseholdRepository;
import com.roomie.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final ProfileRepository profileRepository;
    private final ProfileHouseholdRepository profileHouseholdRepository;

    public HouseholdService(
            HouseholdRepository householdRepository,
            ProfileRepository profileRepository,
            ProfileHouseholdRepository profileHouseholdRepository) {
        this.householdRepository = householdRepository;
        this.profileRepository = profileRepository;
        this.profileHouseholdRepository = profileHouseholdRepository;
    }

    public List<Household> getAllHouseholds() { return householdRepository.findAll(); }

    public Optional<Household> getHouseholdById(Long id) {
        return householdRepository.findById(id);
    }

    public List<Household> getHouseholdsForUser(Long id) { return householdRepository.findByProfileHouseholds_Profile_ProfileId(id); }

    public Household createHousehold(Household household, Long profileId) {
        // Save the household to household repo
        Household savedHousehold = householdRepository.save(household);

        // fetch profile from profile repo
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // set relationship between profile to household
        ProfileHousehold membership = new ProfileHousehold();
        membership.setHousehold(savedHousehold);
        membership.setProfile(profile);
        membership.setPrivs(1); // set user to admin
        membership.setPayInterval(null);

        profileHouseholdRepository.save(membership);

        return savedHousehold;
    }

    public Household updateHousehold(Long id, Household updatedHousehold) {
        Household original = householdRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Household", id));
        original.setRentCost(updatedHousehold.getRentCost());
        original.setNumOfBedrooms(updatedHousehold.getNumOfBedrooms());
        original.setAddress(updatedHousehold.getAddress());
        original.setCity(updatedHousehold.getCity());
        original.setState(updatedHousehold.getState());
        original.setZipCode(updatedHousehold.getZipCode());
        original.setCountry(updatedHousehold.getCountry());
        original.setHouseholdName(updatedHousehold.getHouseholdName());

        return householdRepository.save(original);
    }

    public void deleteHousehold(Long id) {
        if (!householdRepository.existsById(id)) {
            throw new ResourceNotFoundException("Household", id);
        }
        householdRepository.deleteById(id);
    }
}
