package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.dto.HouseholdDTO;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.HouseholdRepository;
import com.roomie.repository.ProfileHouseholdRepository;
import com.roomie.repository.ProfileRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HouseholdServiceTest {

    @Mock
    private HouseholdRepository householdRepository;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private ProfileHouseholdRepository profileHouseholdRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private HouseholdService householdService;

    private Household testHousehold;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testHousehold = new Household();
        setField(testHousehold, "householdId", 1L);
        testHousehold.setHouseholdName("Test House");
        testHousehold.setRentCost(1200f);
        testHousehold.setCity("Carmel");
        testHousehold.setState("CA");

        testProfile = new Profile(
            "John Doe",
            "john@example.com",
            25,
            "oauth-123"
        );
        setField(testProfile, "profileId", 1L);
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
    void getAllHouseholds_returnsListOfDTOs() {
        when(householdRepository.findAll()).thenReturn(List.of(testHousehold));

        List<HouseholdDTO> result = householdService.getAllHouseholds();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHouseholdName()).isEqualTo("Test House");
    }

    @Test
    void getAllHouseholds_returnsEmptyList_whenNoHouseholds() {
        when(householdRepository.findAll()).thenReturn(List.of());

        List<HouseholdDTO> result = householdService.getAllHouseholds();

        assertThat(result).isEmpty();
    }

    @Test
    void getHouseholdById_returnsDTO_whenFound() {
        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );

        HouseholdDTO result = householdService.getHouseholdById(1L);

        assertThat(result.getHouseholdName()).isEqualTo("Test House");
        assertThat(result.getRentCost()).isEqualTo(1200f);
    }

    @Test
    void getHouseholdById_throws_whenNotFound() {
        when(householdRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            householdService.getHouseholdById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateHousehold_updatesFieldsAndReturnsDTO() {
        Household updated = new Household();
        updated.setHouseholdName("New Name");
        updated.setRentCost(1500f);
        updated.setCity("Monterey");

        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(householdRepository.save(any())).thenReturn(testHousehold);

        HouseholdDTO result = householdService.updateHousehold(1L, updated);

        assertThat(result).isNotNull();
        verify(householdRepository, times(1)).save(any());
    }

    @Test
    void updateHousehold_triggersRentExpenseUpdate_whenRentChanges() {
        Household updated = new Household();
        updated.setHouseholdName("Test House");
        updated.setRentCost(1500f); // changed from 1200f

        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(householdRepository.save(any())).thenReturn(testHousehold);

        householdService.updateHousehold(1L, updated);

        verify(expenseService, times(1)).updateRentExpense(any(), eq(1500.0));
    }

    @Test
    void updateHousehold_doesNotTriggerRentUpdate_whenRentUnchanged() {
        Household updated = new Household();
        updated.setHouseholdName("Test House");
        updated.setRentCost(1200f); // same as existing

        when(householdRepository.findById(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(householdRepository.save(any())).thenReturn(testHousehold);

        householdService.updateHousehold(1L, updated);

        verify(expenseService, never()).updateRentExpense(any(), anyDouble());
    }

    @Test
    void updateHousehold_throws_whenNotFound() {
        when(householdRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            householdService.updateHousehold(99L, new Household())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteHousehold_deletesWhenExists() {
        when(householdRepository.existsById(1L)).thenReturn(true);

        householdService.deleteHousehold(1L);

        verify(householdRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteHousehold_throws_whenNotFound() {
        when(householdRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() ->
            householdService.deleteHousehold(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(householdRepository, never()).deleteById(any());
    }

    @Test
    void toDTO_mapsAllFieldsCorrectly() {
        HouseholdDTO dto = householdService.toDTO(testHousehold);

        assertThat(dto.getHouseholdId()).isEqualTo(1L);
        assertThat(dto.getHouseholdName()).isEqualTo("Test House");
        assertThat(dto.getRentCost()).isEqualTo(1200f);
        assertThat(dto.getCity()).isEqualTo("Carmel");
        assertThat(dto.getState()).isEqualTo("CA");
    }
}
