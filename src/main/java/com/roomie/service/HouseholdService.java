package com.roomie.service;

import com.roomie.entity.Household;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.HouseholdRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class HouseholdService {

    private final HouseholdRepository householdRepository;

    public HouseholdService(HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
    }

    public List<Household> getAllHouseholds() { return householdRepository.findAll(); }

    public Optional<Household> getHouseholdById(Long id) {
        return householdRepository.findById(id);
    }

    public List<Household> getHouseholdsForUser(String id) { return householdRepository.findByProfileHouseholds_Profile_OauthId(id); }

    public Household createHousehold(Household household) {
        return householdRepository.save(household);
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
