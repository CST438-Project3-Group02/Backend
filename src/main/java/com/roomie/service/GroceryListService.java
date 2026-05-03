package com.roomie.service;

import com.roomie.dto.GroceryListDTO;
import com.roomie.entity.GroceryList;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.GroceryListRepository;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GroceryListService {

    private final GroceryListRepository groceryListRepository;
    private final ProfileRepository profileRepository;
    private final HouseholdRepository householdRepository;

    public GroceryListService(
        GroceryListRepository groceryListRepository,
        ProfileRepository profileRepository,
        HouseholdRepository householdRepository
    ) {
        this.groceryListRepository = groceryListRepository;
        this.profileRepository = profileRepository;
        this.householdRepository = householdRepository;
    }

    public GroceryListDTO toDTO(GroceryList groceryList) {
        return new GroceryListDTO(
            groceryList.getGroceryListId(),
            groceryList.getListName(),
            groceryList.getProfile(),
            groceryList.getHousehold()
        );
    }

    public List<GroceryListDTO> getAllGroceryLists() {
        return groceryListRepository
            .findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public GroceryListDTO getGroceryListById(Long id) {
        GroceryList groceryList = groceryListRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("GroceryList", id)
            );
        return toDTO(groceryList);
    }

    public GroceryListDTO getGroceryListWithItems(Long id) {
        GroceryList groceryList = groceryListRepository
            .findGroceryListWithItems(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("GroceryList", id)
            );
        GroceryListDTO dto = toDTO(groceryList);
        dto.setGroceryItems(groceryList.getGroceryItems());
        return dto;
    }

    public List<GroceryListDTO> getGroceryListsByHousehold(Long householdId) {
        return groceryListRepository
            .findByHousehold_HouseholdId(householdId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<GroceryListDTO> getGroceryListsWithItemsByHousehold(
        Long householdId
    ) {
        return groceryListRepository
            .findHouseholdListsWithItems(householdId)
            .stream()
            .map(gl -> {
                GroceryListDTO dto = toDTO(gl);
                dto.setGroceryItems(gl.getGroceryItems());
                return dto;
            })
            .collect(Collectors.toList());
    }

    public List<GroceryListDTO> getGroceryListsByProfile(Long profileId) {
        return groceryListRepository
            .findByProfile_ProfileId(profileId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public GroceryList createGroceryList(
        Long profileId,
        Long householdId,
        String listName
    ) {
        Profile profile = profileRepository
            .findById(profileId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Profile", profileId)
            );
        Household household = householdRepository
            .findById(householdId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Household", householdId)
            );

        GroceryList groceryList = new GroceryList(listName);
        groceryList.setProfile(profile);
        groceryList.setHousehold(household);
        return groceryListRepository.save(groceryList);
    }

    public GroceryListDTO updateGroceryList(
        Long id,
        GroceryList updatedGroceryList
    ) {
        GroceryList existing = groceryListRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("GroceryList", id)
            );
        existing.setListName(updatedGroceryList.getListName());
        return toDTO(groceryListRepository.save(existing));
    }

    public void deleteGroceryList(Long id) {
        if (!groceryListRepository.existsById(id)) {
            throw new ResourceNotFoundException("GroceryList", id);
        }
        groceryListRepository.deleteById(id);
    }
}
