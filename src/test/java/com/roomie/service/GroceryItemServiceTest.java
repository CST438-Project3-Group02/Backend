package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.entity.GroceryItem;
import com.roomie.entity.GroceryList;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.GroceryItemRepository;
import com.roomie.repository.GroceryListRepository;
import com.roomie.repository.ProfileRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroceryItemServiceTest {

    @Mock
    private GroceryItemRepository groceryItemRepository;

    @Mock
    private GroceryListRepository groceryListRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private GroceryItemService groceryItemService;

    private GroceryItem testItem;
    private Profile testProfile;
    private GroceryList testGroceryList;

    @BeforeEach
    void setUp() {
        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        setField(testProfile, "profileId", 1L);

        testGroceryList = new GroceryList("Weekly Shop");
        setField(testGroceryList, "groceryListId", 1L);

        testItem = new GroceryItem("Milk", false);
        setField(testItem, "GroceryItemId", 1L);
        testItem.setProfile(testProfile);
        testItem.setGroceryList(testGroceryList);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllGroceryItems_returnsList() {
        when(groceryItemRepository.findAll()).thenReturn(List.of(testItem));

        List<GroceryItem> result = groceryItemService.getAllGroceryItems();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItemName()).isEqualTo("Milk");
    }

    @Test
    void getGroceryItemById_returnsItem_whenFound() {
        when(groceryItemRepository.findById(1L)).thenReturn(
            Optional.of(testItem)
        );

        GroceryItem result = groceryItemService.getGroceryItemById(1L);

        assertThat(result.getItemName()).isEqualTo("Milk");
    }

    @Test
    void getGroceryItemById_throws_whenNotFound() {
        when(groceryItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            groceryItemService.getGroceryItemById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getItemsByGroceryList_returnsItems() {
        when(
            groceryItemRepository.findByGroceryList_GroceryListId(1L)
        ).thenReturn(List.of(testItem));

        List<GroceryItem> result = groceryItemService.getItemsByGroceryList(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getItemsByProfile_returnsItems() {
        when(groceryItemRepository.findByProfile_ProfileId(1L)).thenReturn(
            List.of(testItem)
        );

        List<GroceryItem> result = groceryItemService.getItemsByProfile(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void addGroceryItem_savesAndReturnsItem() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(groceryListRepository.findById(1L)).thenReturn(
            Optional.of(testGroceryList)
        );
        when(groceryItemRepository.save(any())).thenReturn(testItem);

        GroceryItem result = groceryItemService.addGroceryItem(1L, 1L, "Milk");

        assertThat(result.getItemName()).isEqualTo("Milk");
        verify(groceryItemRepository, times(1)).save(any());
    }

    @Test
    void addGroceryItem_throws_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            groceryItemService.addGroceryItem(99L, 1L, "Milk")
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addGroceryItem_throws_whenGroceryListNotFound() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(groceryListRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            groceryItemService.addGroceryItem(1L, 99L, "Milk")
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateGroceryItem_updatesAndReturnsItem() {
        GroceryItem updated = new GroceryItem("Eggs", true);
        when(groceryItemRepository.findById(1L)).thenReturn(
            Optional.of(testItem)
        );
        when(groceryItemRepository.save(any())).thenReturn(testItem);

        GroceryItem result = groceryItemService.updateGroceryItem(1L, updated);

        assertThat(result).isNotNull();
        verify(groceryItemRepository, times(1)).save(any());
    }

    @Test
    void updateGroceryItem_throws_whenNotFound() {
        when(groceryItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            groceryItemService.updateGroceryItem(99L, new GroceryItem())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteGroceryItem_deletesWhenExists() {
        when(groceryItemRepository.existsById(1L)).thenReturn(true);

        groceryItemService.deleteGroceryItem(1L);

        verify(groceryItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGroceryItem_throws_whenNotFound() {
        when(groceryItemRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() ->
            groceryItemService.deleteGroceryItem(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(groceryItemRepository, never()).deleteById(any());
    }
}
