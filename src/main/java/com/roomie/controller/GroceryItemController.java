package com.roomie.controller;

import com.roomie.entity.GroceryItem;
import com.roomie.service.GroceryItemService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grocery-items")
public class GroceryItemController {

    private final GroceryItemService groceryItemService;

    public GroceryItemController(GroceryItemService groceryItemService) {
        this.groceryItemService = groceryItemService;
    }

    @GetMapping
    public ResponseEntity<List<GroceryItem>> getAllGroceryItems() {
        return ResponseEntity.ok(groceryItemService.getAllGroceryItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryItem> getGroceryItemById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(groceryItemService.getGroceryItemById(id));
    }

    @GetMapping("/list/{groceryListId}")
    public ResponseEntity<List<GroceryItem>> getItemsByGroceryList(
        @PathVariable Long groceryListId
    ) {
        return ResponseEntity.ok(
            groceryItemService.getItemsByGroceryList(groceryListId)
        );
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<GroceryItem>> getItemsByProfile(
        @PathVariable Long profileId
    ) {
        return ResponseEntity.ok(
            groceryItemService.getItemsByProfile(profileId)
        );
    }

    @GetMapping("/purchased")
    public ResponseEntity<List<GroceryItem>> getPurchasedItems(
        @RequestParam boolean status
    ) {
        return ResponseEntity.ok(groceryItemService.getPurchasedItems(status));
    }

    @PostMapping
    public ResponseEntity<GroceryItem> addGroceryItem(
        @RequestBody Map<String, Object> payload
    ) {
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        Long groceryListId = Long.valueOf(
            payload.get("groceryListId").toString()
        );
        String itemName = payload.get("itemName").toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(
            groceryItemService.addGroceryItem(
                profileId,
                groceryListId,
                itemName
            )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroceryItem> updateGroceryItem(
        @PathVariable Long id,
        @RequestBody GroceryItem groceryItem
    ) {
        return ResponseEntity.ok(
            groceryItemService.updateGroceryItem(id, groceryItem)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroceryItem(@PathVariable Long id) {
        groceryItemService.deleteGroceryItem(id);
        return ResponseEntity.noContent().build();
    }
}
