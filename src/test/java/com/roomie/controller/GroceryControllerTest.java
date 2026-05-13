package com.roomie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomie.dto.GroceryListDTO;
import com.roomie.entity.GroceryItem;
import com.roomie.entity.GroceryList;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.service.GroceryItemService;
import com.roomie.service.GroceryListService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GroceryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroceryListService groceryListService;

    @MockitoBean
    private GroceryItemService groceryItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private GroceryListDTO testGroceryListDTO;
    private GroceryList testGroceryList;
    private GroceryItem testGroceryItem;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        Household household = new Household();
        household.setHouseholdName("Test House");

        testGroceryList = new GroceryList("Weekly Shop");
        testGroceryList.setProfile(testProfile);
        testGroceryList.setHousehold(household);

        testGroceryListDTO = new GroceryListDTO(
            1L,
            "Weekly Shop",
            testProfile,
            household
        );

        testGroceryItem = new GroceryItem("Milk", false);
        testGroceryItem.setProfile(testProfile);
        testGroceryItem.setGroceryList(testGroceryList);
    }

    @Test
    void getAllGroceryLists_returns200() throws Exception {
        when(groceryListService.getAllGroceryLists()).thenReturn(
            List.of(testGroceryListDTO)
        );

        mockMvc
            .perform(get("/api/grocery-lists"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].listName").value("Weekly Shop"));
    }

    @Test
    void getGroceryListById_returns200_whenFound() throws Exception {
        when(groceryListService.getGroceryListById(1L)).thenReturn(
            testGroceryListDTO
        );

        mockMvc
            .perform(get("/api/grocery-lists/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.listName").value("Weekly Shop"));
    }

    @Test
    void getGroceryListById_returns404_whenNotFound() throws Exception {
        when(groceryListService.getGroceryListById(99L)).thenThrow(
            new ResourceNotFoundException("GroceryList", 99L)
        );

        mockMvc
            .perform(get("/api/grocery-lists/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getGroceryListWithItems_returns200() throws Exception {
        when(groceryListService.getGroceryListWithItems(1L)).thenReturn(
            testGroceryListDTO
        );

        mockMvc
            .perform(get("/api/grocery-lists/1/items"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.listName").value("Weekly Shop"));
    }

    @Test
    void getGroceryListsByHousehold_returns200() throws Exception {
        when(groceryListService.getGroceryListsByHousehold(1L)).thenReturn(
            List.of(testGroceryListDTO)
        );

        mockMvc
            .perform(get("/api/grocery-lists/household/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].listName").value("Weekly Shop"));
    }

    @Test
    void getGroceryListsWithItemsByHousehold_returns200() throws Exception {
        when(
            groceryListService.getGroceryListsWithItemsByHousehold(1L)
        ).thenReturn(List.of(testGroceryListDTO));

        mockMvc
            .perform(get("/api/grocery-lists/household/1/full"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].listName").value("Weekly Shop"));
    }

    @Test
    void getGroceryListsByProfile_returns200() throws Exception {
        when(groceryListService.getGroceryListsByProfile(1L)).thenReturn(
            List.of(testGroceryListDTO)
        );

        mockMvc
            .perform(get("/api/grocery-lists/profile/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].listName").value("Weekly Shop"));
    }

    @Test
    void createGroceryList_returns201() throws Exception {
        when(
            groceryListService.createGroceryList(1L, 1L, "Weekly Shop")
        ).thenReturn(testGroceryList);
        when(groceryListService.toDTO(any())).thenReturn(testGroceryListDTO);

        Map<String, Object> payload = Map.of(
            "profileId",
            1,
            "householdId",
            1,
            "listName",
            "Weekly Shop"
        );

        mockMvc
            .perform(
                post("/api/grocery-lists")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.listName").value("Weekly Shop"));
    }

    @Test
    void createGroceryList_returns404_whenProfileNotFound() throws Exception {
        when(
            groceryListService.createGroceryList(99L, 1L, "Weekly Shop")
        ).thenThrow(new ResourceNotFoundException("Profile", 99L));

        Map<String, Object> payload = Map.of(
            "profileId",
            99,
            "householdId",
            1,
            "listName",
            "Weekly Shop"
        );

        mockMvc
            .perform(
                post("/api/grocery-lists")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteGroceryList_returns204() throws Exception {
        doNothing().when(groceryListService).deleteGroceryList(1L);

        mockMvc
            .perform(delete("/api/grocery-lists/1"))
            .andExpect(status().isNoContent());

        verify(groceryListService, times(1)).deleteGroceryList(1L);
    }

    @Test
    void deleteGroceryList_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("GroceryList", 99L))
            .when(groceryListService)
            .deleteGroceryList(99L);

        mockMvc
            .perform(delete("/api/grocery-lists/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getAllGroceryItems_returns200() throws Exception {
        when(groceryItemService.getAllGroceryItems()).thenReturn(
            List.of(testGroceryItem)
        );

        mockMvc
            .perform(get("/api/grocery-items"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].itemName").value("Milk"));
    }

    @Test
    void getGroceryItemById_returns200_whenFound() throws Exception {
        when(groceryItemService.getGroceryItemById(1L)).thenReturn(
            testGroceryItem
        );

        mockMvc
            .perform(get("/api/grocery-items/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.itemName").value("Milk"));
    }

    @Test
    void getGroceryItemById_returns404_whenNotFound() throws Exception {
        when(groceryItemService.getGroceryItemById(99L)).thenThrow(
            new ResourceNotFoundException("GroceryItem", 99L)
        );

        mockMvc
            .perform(get("/api/grocery-items/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getItemsByGroceryList_returns200() throws Exception {
        when(groceryItemService.getItemsByGroceryList(1L)).thenReturn(
            List.of(testGroceryItem)
        );

        mockMvc
            .perform(get("/api/grocery-items/list/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].itemName").value("Milk"));
    }

    @Test
    void getItemsByProfile_returns200() throws Exception {
        when(groceryItemService.getItemsByProfile(1L)).thenReturn(
            List.of(testGroceryItem)
        );

        mockMvc
            .perform(get("/api/grocery-items/profile/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].itemName").value("Milk"));
    }

    @Test
    void addGroceryItem_returns201() throws Exception {
        when(groceryItemService.addGroceryItem(1L, 1L, "Milk")).thenReturn(
            testGroceryItem
        );

        Map<String, Object> payload = Map.of(
            "profileId",
            1,
            "groceryListId",
            1,
            "itemName",
            "Milk"
        );

        mockMvc
            .perform(
                post("/api/grocery-items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.itemName").value("Milk"));
    }

    @Test
    void addGroceryItem_returns404_whenProfileNotFound() throws Exception {
        when(groceryItemService.addGroceryItem(99L, 1L, "Milk")).thenThrow(
            new ResourceNotFoundException("Profile", 99L)
        );

        Map<String, Object> payload = Map.of(
            "profileId",
            99,
            "groceryListId",
            1,
            "itemName",
            "Milk"
        );

        mockMvc
            .perform(
                post("/api/grocery-items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteGroceryItem_returns204() throws Exception {
        doNothing().when(groceryItemService).deleteGroceryItem(1L);

        mockMvc
            .perform(delete("/api/grocery-items/1"))
            .andExpect(status().isNoContent());

        verify(groceryItemService, times(1)).deleteGroceryItem(1L);
    }

    @Test
    void deleteGroceryItem_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("GroceryItem", 99L))
            .when(groceryItemService)
            .deleteGroceryItem(99L);

        mockMvc
            .perform(delete("/api/grocery-items/99"))
            .andExpect(status().isNotFound());
    }
}
