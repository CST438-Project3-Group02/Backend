package com.roomie.service;

import com.roomie.dto.HouseholdDTO;
import com.roomie.dto.ProfileDTO;
import com.roomie.entity.Household;
import com.roomie.entity.ProfileHousehold;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.HouseholdRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class HouseholdService {

    private final HouseholdRepository householdRepository;

    public HouseholdService(HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
    }

    private HouseholdDTO toDTO(Household household) {
        return new HouseholdDTO(
            household.getHouseholdId(),
            household.getHouseholdName(),
            household.getRentCost(),
            household.getNumOfBedrooms(),
            household.getAddress(),
            household.getCity(),
            household.getState(),
            household.getZipCode(),
            household.getCountry()
        );
    }

    public List<HouseholdDTO> getAllHouseholds() {
        return householdRepository
            .findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public HouseholdDTO getHouseholdById(Long id) {
        Household household = householdRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Household", id));
        return toDTO(household);
    }

    public HouseholdDTO getHouseholdWithProfiles(Long id) {
        Household household = householdRepository
            .findHouseholdWithProfiles(id)
            .orElseThrow(() -> new ResourceNotFoundException("Household", id));
        HouseholdDTO dto = toDTO(household);

        List<ProfileDTO> profileDTOs = household
                .getProfileHouseholds()
                .stream()
                .map(ph -> {
                    ProfileDTO profileDTO = new ProfileDTO(
                            ph.getProfile().getProfileId(),
                            ph.getProfile().getName(),
                            ph.getProfile().getEmail(),
                            ph.getProfile().getAge(),
                            ph.getProfile().getProfilePicUrl()
                    );
                    profileDTO.setMemberships(ph); // attach the membership
                    return profileDTO;
                })
                .collect(Collectors.toList());

        dto.setProfiles(profileDTOs);
        return dto;
    }

    public HouseholdDTO getHouseholdWithChores(Long id) {
        Household household = householdRepository
            .findHouseholdWithChores(id)
            .orElseThrow(() -> new ResourceNotFoundException("Household", id));
        HouseholdDTO dto = toDTO(household);
        dto.setChores(household.getChores());
        return dto;
    }

    public HouseholdDTO getHouseholdWithGroceryLists(Long id) {
        Household household = householdRepository
            .findHouseholdWithGroceryLists(id)
            .orElseThrow(() -> new ResourceNotFoundException("Household", id));
        HouseholdDTO dto = toDTO(household);
        dto.setGroceryLists(household.getGroceryLists());
        return dto;
    }

    public HouseholdDTO getHouseholdWithExpenses(Long id) {
        Household household = householdRepository
            .findHouseholdWithExpenses(id)
            .orElseThrow(() -> new ResourceNotFoundException("Household", id));
        HouseholdDTO dto = toDTO(household);
        dto.setExpenses(household.getExpenses());
        return dto;
    }

    public HouseholdDTO getHouseholdWithInvites(Long id) {
        Household household = householdRepository
            .findHouseholdWithInvites(id)
            .orElseThrow(() -> new ResourceNotFoundException("Household", id));
        HouseholdDTO dto = toDTO(household);
        dto.setInvites(household.getInvites());
        return dto;
    }

    public Household createHousehold(Household household) {
        return householdRepository.save(household);
    }

    public HouseholdDTO updateHousehold(Long id, Household updatedHousehold) {
        Household existing = householdRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Household", id));
        existing.setHouseholdName(updatedHousehold.getHouseholdName());
        existing.setRentCost(updatedHousehold.getRentCost());
        existing.setNumOfBedrooms(updatedHousehold.getNumOfBedrooms());
        existing.setAddress(updatedHousehold.getAddress());
        existing.setCity(updatedHousehold.getCity());
        existing.setState(updatedHousehold.getState());
        existing.setZipCode(updatedHousehold.getZipCode());
        existing.setCountry(updatedHousehold.getCountry());
        return toDTO(householdRepository.save(existing));
    }

    public void deleteHousehold(Long id) {
        if (!householdRepository.existsById(id)) {
            throw new ResourceNotFoundException("Household", id);
        }
        householdRepository.deleteById(id);
    }
}
