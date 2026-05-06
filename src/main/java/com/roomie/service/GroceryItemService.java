package com.roomie.service;

import com.roomie.entity.GroceryItem;
import com.roomie.entity.GroceryList;
import com.roomie.entity.Profile;
import com.roomie.enums.ActivityType;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.GroceryItemRepository;
import com.roomie.repository.GroceryListRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GroceryItemService {

    private final GroceryItemRepository groceryItemRepository;
    private final GroceryListRepository groceryListRepository;
    private final ProfileRepository profileRepository;

    // private final ActivityEventService activityEventService;

    public GroceryItemService(
        GroceryItemRepository groceryItemRepository,
        GroceryListRepository groceryListRepository,
        ProfileRepository profileRepository
        // ActivityEventService activityEventService
    ) {
        this.groceryItemRepository = groceryItemRepository;
        this.groceryListRepository = groceryListRepository;
        this.profileRepository = profileRepository;
        // this.activityEventService = activityEventService;
    }

    public List<GroceryItem> getAllGroceryItems() {
        return groceryItemRepository.findAll();
    }

    public GroceryItem getGroceryItemById(Long id) {
        return groceryItemRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("GroceryItem", id)
            );
    }

    public List<GroceryItem> getItemsByGroceryList(Long groceryListId) {
        return groceryItemRepository.findByGroceryList_GroceryListId(
            groceryListId
        );
    }

    public List<GroceryItem> getItemsByProfile(Long profileId) {
        return groceryItemRepository.findByProfile_ProfileId(profileId);
    }

    public List<GroceryItem> getPurchasedItems(boolean isPurchased) {
        return groceryItemRepository.findByIsPurchased(isPurchased);
    }

    public GroceryItem addGroceryItem(
        Long profileId,
        Long groceryListId,
        String itemName
    ) {
        Profile profile = profileRepository
            .findById(profileId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Profile", profileId)
            );
        GroceryList groceryList = groceryListRepository
            .findById(groceryListId)
            .orElseThrow(() ->
                new ResourceNotFoundException("GroceryList", groceryListId)
            );

        GroceryItem groceryItem = new GroceryItem(itemName, false);
        groceryItem.setProfile(profile);
        groceryItem.setGroceryList(groceryList);
        // activityEventService.log(
        //     profileId,
        //     groceryList.getHousehold().getHouseholdId(),
        //     ActivityType.GROCERY_ITEM_ADDED,
        //     false
        // );
        return groceryItemRepository.save(groceryItem);
    }

    public GroceryItem updateGroceryItem(Long id, GroceryItem updatedItem) {
        GroceryItem existing = groceryItemRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("GroceryItem", id)
            );
        existing.setItemName(updatedItem.getItemName());
        existing.setIsPurchased(updatedItem.isPurchased());
        // if (updatedItem.isPurchased()) {
        //     activityEventService.log(
        //         existing.getProfile().getProfileId(),
        //         existing.getGroceryList().getHousehold().getHouseholdId(),
        //         ActivityType.GROCERY_ITEM_PURCHASED,
        //         true
        //     );
        // }
        return groceryItemRepository.save(existing);
    }

    public void deleteGroceryItem(Long id) {
        if (!groceryItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("GroceryItem", id);
        }
        groceryItemRepository.deleteById(id);
    }
}
