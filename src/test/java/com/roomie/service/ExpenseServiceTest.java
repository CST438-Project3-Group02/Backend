package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.dto.ExpenseDTO;
import com.roomie.entity.Expense;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.BillRepository;
import com.roomie.repository.ExpenseRepository;
import com.roomie.repository.HouseholdRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private HouseholdRepository householdRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private ActivityEventService activityEventService;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense testExpense;
    private Household testHousehold;
    private Profile testProfile;

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

        ProfileHousehold ph = new ProfileHousehold();
        ph.setProfile(testProfile);
        ph.setHousehold(testHousehold);
        testHousehold.setProfileHouseholds(List.of(ph));

        testExpense = new Expense("Rent", 1200.0, 100.0, false, null);
        setField(testExpense, "expenseId", 1L);
        testExpense.setHousehold(testHousehold);
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
    void getAllExpenses_returnsListOfDTOs() {
        when(expenseRepository.findAll()).thenReturn(List.of(testExpense));

        List<ExpenseDTO> result = expenseService.getAllExpenses();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Rent");
    }

    @Test
    void getAllExpenses_returnsEmptyList_whenNone() {
        when(expenseRepository.findAll()).thenReturn(List.of());

        assertThat(expenseService.getAllExpenses()).isEmpty();
    }

    @Test
    void getExpenseById_returnsDTO_whenFound() {
        when(expenseRepository.findById(1L)).thenReturn(
            Optional.of(testExpense)
        );

        ExpenseDTO result = expenseService.getExpenseById(1L);

        assertThat(result.getDescription()).isEqualTo("Rent");
        assertThat(result.getAmount()).isEqualTo(1200.0);
    }

    @Test
    void getExpenseById_throws_whenNotFound() {
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            expenseService.getExpenseById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getExpensesByHousehold_returnsDTOs() {
        when(expenseRepository.findByHousehold_HouseholdId(1L)).thenReturn(
            List.of(testExpense)
        );

        List<ExpenseDTO> result = expenseService.getExpensesByHousehold(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHouseholdId()).isEqualTo(1L);
    }

    @Test
    void createExpense_withMembersPassed_savesExpenseAndBills() {
        when(householdRepository.findHouseholdWithProfiles(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(expenseRepository.save(any())).thenReturn(testExpense);

        ExpenseDTO result = expenseService.createExpense(
            1L,
            "Electricity",
            100.0,
            null,
            List.of(testProfile)
        );

        assertThat(result).isNotNull();
        verify(expenseRepository, times(1)).save(any());
        verify(billRepository, times(1)).save(any());
    }

    @Test
    void createExpense_withNullMembers_queriesHousehold() {
        when(householdRepository.findHouseholdWithProfiles(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(expenseRepository.save(any())).thenReturn(testExpense);

        ExpenseDTO result = expenseService.createExpense(
            1L,
            "Electricity",
            100.0,
            null,
            null
        );

        assertThat(result).isNotNull();
        verify(expenseRepository, times(1)).save(any());
    }

    @Test
    void createExpense_withEmptyMembers_skipsBillGeneration() {
        when(householdRepository.findHouseholdWithProfiles(1L)).thenReturn(
            Optional.of(testHousehold)
        );
        when(expenseRepository.save(any())).thenReturn(testExpense);

        expenseService.createExpense(
            1L,
            "Electricity",
            100.0,
            null,
            new ArrayList<>()
        );

        verify(billRepository, never()).save(any());
    }

    @Test
    void createExpense_throws_whenHouseholdNotFound() {
        when(householdRepository.findHouseholdWithProfiles(99L)).thenReturn(
            Optional.empty()
        );

        assertThatThrownBy(() ->
            expenseService.createExpense(99L, "Rent", 1200.0, null, null)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateExpense_updatesFieldsAndReturnsDTO() {
        Expense updated = new Expense("Internet", 60.0, 50.0, false, null);
        when(expenseRepository.findById(1L)).thenReturn(
            Optional.of(testExpense)
        );
        when(expenseRepository.save(any())).thenReturn(testExpense);

        ExpenseDTO result = expenseService.updateExpense(1L, updated);

        assertThat(result).isNotNull();
        verify(expenseRepository, times(1)).save(any());
    }

    @Test
    void updateExpense_throws_whenNotFound() {
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
            expenseService.updateExpense(99L, new Expense())
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void markPaid_setsPaidTrueAndReturnsDTO() {
        when(expenseRepository.findById(1L)).thenReturn(
            Optional.of(testExpense)
        );
        when(expenseRepository.save(any())).thenReturn(testExpense);

        ExpenseDTO result = expenseService.markPaid(1L);

        assertThat(result).isNotNull();
        verify(expenseRepository, times(1)).save(any());
    }

    @Test
    void markPaid_throws_whenNotFound() {
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseService.markPaid(99L)).isInstanceOf(
            ResourceNotFoundException.class
        );
    }

    @Test
    void deleteExpense_deletesWhenExists() {
        when(expenseRepository.existsById(1L)).thenReturn(true);

        expenseService.deleteExpense(1L);

        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExpense_throws_whenNotFound() {
        when(expenseRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() ->
            expenseService.deleteExpense(99L)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(expenseRepository, never()).deleteById(any());
    }

    @Test
    void toDTO_mapsAllFieldsCorrectly() {
        ExpenseDTO dto = expenseService.toDTO(testExpense);

        assertThat(dto.getExpenseId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Rent");
        assertThat(dto.getAmount()).isEqualTo(1200.0);
        assertThat(dto.getPaid()).isFalse();
        assertThat(dto.getHouseholdId()).isEqualTo(1L);
        assertThat(dto.getHouseholdName()).isEqualTo("Test House");
    }
}
