package com.roomie.controller;

import com.roomie.dto.GroceryListDTO;
import com.roomie.entity.GroceryList;
import com.roomie.service.GroceryListService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grocery-lists")
public class GroceryListController {

    private final GroceryListService groceryListService;

    public GroceryListController(GroceryListService groceryListService) {
        this.groceryListService = groceryListService;
    }

    @GetMapping
    public ResponseEntity<List<GroceryListDTO>> getAllGroceryLists() {
        return ResponseEntity.ok(groceryListService.getAllGroceryLists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryListDTO> getGroceryListById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(groceryListService.getGroceryListById(id));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<GroceryListDTO> getGroceryListWithItems(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            groceryListService.getGroceryListWithItems(id)
        );
    }

    @GetMapping("/household/{householdId}")
    public ResponseEntity<List<GroceryListDTO>> getGroceryListsByHousehold(
        @PathVariable Long householdId
    ) {
        return ResponseEntity.ok(
            groceryListService.getGroceryListsByHousehold(householdId)
        );
    }

    // GET /api/grocery-lists/household/1/full — all lists with items populated
    @GetMapping("/household/{householdId}/full")
    public ResponseEntity<
        List<GroceryListDTO>
    > getGroceryListsWithItemsByHousehold(@PathVariable Long householdId) {
        return ResponseEntity.ok(
            groceryListService.getGroceryListsWithItemsByHousehold(householdId)
        );
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<GroceryListDTO>> getGroceryListsByProfile(
        @PathVariable Long profileId
    ) {
        return ResponseEntity.ok(
            groceryListService.getGroceryListsByProfile(profileId)
        );
    }

    @PostMapping
    public ResponseEntity<GroceryListDTO> createGroceryList(
        @RequestBody Map<String, Object> payload
    ) {
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        Long householdId = Long.valueOf(payload.get("householdId").toString());
        String listName = payload.get("listName").toString();
        GroceryList created = groceryListService.createGroceryList(
            profileId,
            householdId,
            listName
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(
            groceryListService.toDTO(created)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroceryListDTO> updateGroceryList(
        @PathVariable Long id,
        @RequestBody GroceryList groceryList
    ) {
        return ResponseEntity.ok(
            groceryListService.updateGroceryList(id, groceryList)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroceryList(@PathVariable Long id) {
        groceryListService.deleteGroceryList(id);
        return ResponseEntity.noContent().build();
    }
}
