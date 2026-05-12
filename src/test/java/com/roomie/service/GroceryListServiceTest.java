package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.dto.GroceryListDTO;
import com.roomie.entity.GroceryList;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.GroceryListRepository;
import com.roomie.repository.HouseholdRepository;
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
class GroceryListServiceTest {

    @Mock
    private GroceryListRepository groceryListRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HouseholdRepository householdRepository;

    @InjectMocks
    private GroceryListService groceryListService;

    private GroceryList testList;
    private Profile testProfile;
    private Household testHousehold;

    @BeforeEach
    void setUp() {
        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        setField(testProfile, "profileId", 1L);

        testHousehold = new Household();
        setField(testHousehold, "householdId", 1L);
        testHousehold.setHouseholdName("Test House");

        testList = new GroceryList("Weekly Shop");
        setField(testList, "groceryListId", 1L);
        testList.setProfile(testProfile);
        testList.setHousehold(testHousehold);
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
    void getAllGroceryLists_returnsDTOs() {
        when(groceryListRepository.findAll()).thenReturn(List.of(testList));

        List<GroceryListDTO> result = groceryListService.getAllGroceryLists();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getListName()).isEqualTo("Weekly Shop");
    }

    @Test
    void getAllGroceryLists_returnsEmpty_whenNone() {
        when(groceryListRepository.findAll()).thenReturn(List.of());

        assertThat(groceryListService.getAllGroceryLists()).isEmpty();
    }

    @Test
    void getGroceryListById_returnsDTO_whenFound() {
        when(groceryListRepository.findById(1L)).thenReturn(
            Optional.of(testList)
        );

        GroceryListDTO result = groceryListService.getGroceryListById(1L);

        assertThat(result.getListName()).isEqualTo("Weekly Shop");
        assertThat(result.getProfileId()).isEqualTo(1L);
        assertThat(result.getHouseholdId()).isEqualTo(1L);
    }

    @Test
    void getGroceryListById_throws_whenNotFound() {
        when(groceryListRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            groceryListService.getGroceryListById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getGroceryListsByHousehold_returnsDTOs() {
        when(groceryListRepository.findByHousehold_HouseholdId(1L)).thenReturn(
            List.of(testList)
        );

        List<GroceryListDTO> result =
            groceryListService.getGroceryListsByHousehold(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHouseholdId()).isEqualTo(1L);
    }

    @Test
    void getGroceryListsByProfile_returnsDTOs() {
        when(groceryListRepository.findByProfile_ProfileId(1L)).thenReturn(
            List.of(testList)
        );

        List<GroceryListDTO> result =
            groceryListService.getGroceryListsByProfile(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProfileId()).isEqualTo(1L);
    }

    @Test
    void createGroceryList_savesAndReturns() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(groceryListRepository.save(any())).thenReturn(testList);

        GroceryList result = groceryListService.createGroceryList(
            1L,
            1L,
            "Weekly Shop"
        );

        assertThat(result.getListName()).isEqualTo("Weekly Shop");
        verify(groceryListRepository, times(1)).save(any());
    }

    @Test
    void createGroceryList_throws_whenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            groceryListService.createGroceryList(99L, 1L, "List")
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createGroceryList_throws_whenHouseholdNotFound() {
        when(profileRepository.findById(1L)).thenReturn(
            Optional.of(testProfile)
        );
        when(householdRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            groceryListService.createGroceryList(1L, 99L, "List")
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateGroceryList_updatesNameAndReturnsDTO() {
        GroceryList updated = new GroceryList("Monthly Shop");
        when(groceryListRepository.findById(1L)).thenReturn(
            Optional.of(testList)
        );
        when(groceryListRepository.save(any())).thenReturn(testList);

        GroceryListDTO result = groceryListService.updateGroceryList(
            1L,
            updated
        );

        assertThat(result).isNotNull();
        verify(groceryListRepository, times(1)).save(any());
    }

    @Test
    void updateGroceryList_throws_whenNotFound() {
        when(groceryListRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            groceryListService.updateGroceryList(99L, new GroceryList())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteGroceryList_deletesWhenExists() {
        when(groceryListRepository.existsById(1L)).thenReturn(true);

        groceryListService.deleteGroceryList(1L);

        verify(groceryListRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGroceryList_throws_whenNotFound() {
        when(groceryListRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() ->
            groceryListService.deleteGroceryList(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(groceryListRepository, never()).deleteById(any());
    }
}
