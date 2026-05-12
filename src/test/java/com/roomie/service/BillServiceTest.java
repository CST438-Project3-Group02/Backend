package com.roomie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roomie.dto.BillDTO;
import com.roomie.entity.Bill;
import com.roomie.entity.Expense;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.enums.ActivityType;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.BillRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private ActivityEventService activityEventService;

    @InjectMocks
    private BillService billService;

    private Bill testBill;
    private Profile testProfile;
    private Expense testExpense;
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

        testExpense = new Expense("Rent", 1200.0, 100.0, false, null);
        setField(testExpense, "expenseId", 1L);
        testExpense.setHousehold(testHousehold);

        testBill = new Bill("Rent", 600.0, false, null, testProfile);
        setField(testBill, "billId", 1L);
        testBill.setExpense(testExpense);
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

    // ─── getBillById ──────────────────────────────────────────────────────────

    @Test
    void getBillById_returnsDTO_whenFound() {
        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));

        BillDTO result = billService.getBillById(1L);

        assertThat(result.getDescription()).isEqualTo("Rent");
        assertThat(result.getAmount()).isEqualTo(600.0);
        assertThat(result.getProfileId()).isEqualTo(1L);
    }

    @Test
    void getBillById_throws_whenNotFound() {
        when(billRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.getBillById(99L)).isInstanceOf(
            ResourceNotFoundException.class
        );
    }

    // ─── getBillsByProfile ────────────────────────────────────────────────────

    @Test
    void getBillsByProfile_returnsDTOs() {
        when(billRepository.findByProfile_ProfileId(1L)).thenReturn(
            List.of(testBill)
        );

        List<BillDTO> result = billService.getBillsByProfile(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProfileId()).isEqualTo(1L);
    }

    // ─── getBillsByExpense ────────────────────────────────────────────────────

    @Test
    void getBillsByExpense_returnsDTOs() {
        when(billRepository.findByExpense_ExpenseId(1L)).thenReturn(
            List.of(testBill)
        );

        List<BillDTO> result = billService.getBillsByExpense(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getExpenseId()).isEqualTo(1L);
    }

    // ─── getBillsByProfileAndStatus ───────────────────────────────────────────

    @Test
    void getBillsByProfileAndStatus_returnsDTOs() {
        when(
            billRepository.findByProfile_ProfileIdAndPaid(1L, false)
        ).thenReturn(List.of(testBill));

        List<BillDTO> result = billService.getBillsByProfileAndStatus(
            1L,
            false
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPaid()).isFalse();
    }

    // ─── getBillsByProfileAndHousehold ────────────────────────────────────────

    @Test
    void getBillsByProfileAndHousehold_returnsDTOs() {
        when(billRepository.findByProfileIdAndHouseholdId(1L, 1L)).thenReturn(
            List.of(testBill)
        );

        List<BillDTO> result = billService.getBillsByProfileAndHousehold(
            1L,
            1L
        );

        assertThat(result).hasSize(1);
    }

    // ─── markPaid ─────────────────────────────────────────────────────────────

    @Test
    void markPaid_setsPaidTrueAndReturnsDTO() {
        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));
        when(billRepository.save(any())).thenReturn(testBill);

        BillDTO result = billService.markPaid(1L);

        assertThat(result).isNotNull();
        verify(billRepository, times(1)).save(any());
    }

    @Test
    void markPaid_logsActivity() {
        when(billRepository.findById(1L)).thenReturn(Optional.of(testBill));
        when(billRepository.save(any())).thenReturn(testBill);

        billService.markPaid(1L);

        verify(activityEventService, times(1)).log(
            eq(1L),
            eq(1L),
            eq(ActivityType.BILL_PAID),
            eq(true)
        );
    }

    @Test
    void markPaid_throws_whenNotFound() {
        when(billRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.markPaid(99L)).isInstanceOf(
            ResourceNotFoundException.class
        );
    }

    // ─── deleteBill ───────────────────────────────────────────────────────────

    @Test
    void deleteBill_deletesWhenExists() {
        when(billRepository.existsById(1L)).thenReturn(true);

        billService.deleteBill(1L);

        verify(billRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBill_throws_whenNotFound() {
        when(billRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> billService.deleteBill(99L)).isInstanceOf(
            ResourceNotFoundException.class
        );

        verify(billRepository, never()).deleteById(any());
    }

    // ─── toDTO ────────────────────────────────────────────────────────────────

    @Test
    void toDTO_mapsAllFieldsCorrectly() {
        BillDTO dto = billService.toDTO(testBill);

        assertThat(dto.getBillId()).isEqualTo(1L);
        assertThat(dto.getDescription()).isEqualTo("Rent");
        assertThat(dto.getAmount()).isEqualTo(600.0);
        assertThat(dto.getPaid()).isFalse();
        assertThat(dto.getProfileId()).isEqualTo(1L);
        assertThat(dto.getProfileName()).isEqualTo("John Doe");
        assertThat(dto.getExpenseId()).isEqualTo(1L);
    }
}
