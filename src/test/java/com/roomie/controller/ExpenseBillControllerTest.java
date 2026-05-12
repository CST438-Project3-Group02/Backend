package com.roomie.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roomie.dto.BillDTO;
import com.roomie.dto.ExpenseDTO;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.service.BillService;
import com.roomie.service.ExpenseService;
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
class ExpenseBillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @MockitoBean
    private BillService billService;

    @Autowired
    private ObjectMapper objectMapper;

    private ExpenseDTO testExpenseDTO;
    private BillDTO testBillDTO;

    @BeforeEach
    void setUp() {
        testExpenseDTO = new ExpenseDTO(
            1L,
            "Rent",
            1200.0,
            100.0,
            false,
            null,
            null,
            1L,
            "Test House"
        );
        testBillDTO = new BillDTO(
            1L,
            "Rent",
            600.0,
            false,
            null,
            null,
            1L,
            "John Doe",
            1L
        );
    }

    // ═══ ExpenseController ════════════════════════════════════════════════════

    @Test
    void getAllExpenses_returns200() throws Exception {
        when(expenseService.getAllExpenses()).thenReturn(
            List.of(testExpenseDTO)
        );

        mockMvc
            .perform(get("/api/expenses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Rent"));
    }

    @Test
    void getExpenseById_returns200_whenFound() throws Exception {
        when(expenseService.getExpenseById(1L)).thenReturn(testExpenseDTO);

        mockMvc
            .perform(get("/api/expenses/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Rent"))
            .andExpect(jsonPath("$.amount").value(1200.0));
    }

    @Test
    void getExpenseById_returns404_whenNotFound() throws Exception {
        when(expenseService.getExpenseById(99L)).thenThrow(
            new ResourceNotFoundException("Expense", 99L)
        );

        mockMvc
            .perform(get("/api/expenses/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getExpensesByHousehold_returns200() throws Exception {
        when(expenseService.getExpensesByHousehold(1L)).thenReturn(
            List.of(testExpenseDTO)
        );

        mockMvc
            .perform(get("/api/expenses/household/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Rent"));
    }

    @Test
    void getExpensesWithBillsByHousehold_returns200() throws Exception {
        when(expenseService.getExpensesWithBillsByHousehold(1L)).thenReturn(
            List.of(testExpenseDTO)
        );

        mockMvc
            .perform(get("/api/expenses/household/1/full"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Rent"));
    }

    @Test
    void getExpensesByHouseholdAndStatus_returns200() throws Exception {
        when(
            expenseService.getExpensesByHouseholdAndStatus(1L, false)
        ).thenReturn(List.of(testExpenseDTO));

        mockMvc
            .perform(
                get("/api/expenses/household/1/status").param("paid", "false")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].paid").value(false));
    }

    @Test
    void createExpense_returns201() throws Exception {
        when(
            expenseService.createExpense(
                eq(1L),
                eq("Rent"),
                eq(1200.0),
                any(),
                any()
            )
        ).thenReturn(testExpenseDTO);

        Map<String, Object> payload = Map.of(
            "householdId",
            1,
            "description",
            "Rent",
            "amount",
            1200.0
        );

        mockMvc
            .perform(
                post("/api/expenses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.description").value("Rent"));
    }

    @Test
    void createExpense_returns404_whenHouseholdNotFound() throws Exception {
        when(
            expenseService.createExpense(eq(99L), any(), any(), any(), any())
        ).thenThrow(new ResourceNotFoundException("Household", 99L));

        Map<String, Object> payload = Map.of(
            "householdId",
            99,
            "description",
            "Rent",
            "amount",
            1200.0
        );

        mockMvc
            .perform(
                post("/api/expenses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payload))
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void markExpensePaid_returns200() throws Exception {
        when(expenseService.markPaid(1L)).thenReturn(testExpenseDTO);

        mockMvc
            .perform(patch("/api/expenses/1/paid"))
            .andExpect(status().isOk());
    }

    @Test
    void markExpensePaid_returns404_whenNotFound() throws Exception {
        when(expenseService.markPaid(99L)).thenThrow(
            new ResourceNotFoundException("Expense", 99L)
        );

        mockMvc
            .perform(patch("/api/expenses/99/paid"))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpense_returns204() throws Exception {
        doNothing().when(expenseService).deleteExpense(1L);

        mockMvc
            .perform(delete("/api/expenses/1"))
            .andExpect(status().isNoContent());

        verify(expenseService, times(1)).deleteExpense(1L);
    }

    @Test
    void deleteExpense_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Expense", 99L))
            .when(expenseService)
            .deleteExpense(99L);

        mockMvc
            .perform(delete("/api/expenses/99"))
            .andExpect(status().isNotFound());
    }

    // ═══ BillController ═══════════════════════════════════════════════════════

    @Test
    void getBillById_returns200_whenFound() throws Exception {
        when(billService.getBillById(1L)).thenReturn(testBillDTO);

        mockMvc
            .perform(get("/api/bills/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value("Rent"))
            .andExpect(jsonPath("$.amount").value(600.0));
    }

    @Test
    void getBillById_returns404_whenNotFound() throws Exception {
        when(billService.getBillById(99L)).thenThrow(
            new ResourceNotFoundException("Bill", 99L)
        );

        mockMvc.perform(get("/api/bills/99")).andExpect(status().isNotFound());
    }

    @Test
    void getBillsByProfile_returns200() throws Exception {
        when(billService.getBillsByProfile(1L)).thenReturn(
            List.of(testBillDTO)
        );

        mockMvc
            .perform(get("/api/bills/profile/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].profileId").value(1));
    }

    @Test
    void getBillsByProfileAndStatus_returns200() throws Exception {
        when(billService.getBillsByProfileAndStatus(1L, false)).thenReturn(
            List.of(testBillDTO)
        );

        mockMvc
            .perform(get("/api/bills/profile/1/status").param("paid", "false"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].paid").value(false));
    }

    @Test
    void getBillsByExpense_returns200() throws Exception {
        when(billService.getBillsByExpense(1L)).thenReturn(
            List.of(testBillDTO)
        );

        mockMvc
            .perform(get("/api/bills/expense/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].expenseId").value(1));
    }

    @Test
    void getBillsByProfileAndHousehold_returns200() throws Exception {
        when(billService.getBillsByProfileAndHousehold(1L, 1L)).thenReturn(
            List.of(testBillDTO)
        );

        mockMvc
            .perform(get("/api/bills/profile/1/household/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Rent"));
    }

    @Test
    void markBillPaid_returns200() throws Exception {
        when(billService.markPaid(1L)).thenReturn(testBillDTO);

        mockMvc.perform(patch("/api/bills/1/paid")).andExpect(status().isOk());
    }

    @Test
    void markBillPaid_returns404_whenNotFound() throws Exception {
        when(billService.markPaid(99L)).thenThrow(
            new ResourceNotFoundException("Bill", 99L)
        );

        mockMvc
            .perform(patch("/api/bills/99/paid"))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteBill_returns204() throws Exception {
        doNothing().when(billService).deleteBill(1L);

        mockMvc
            .perform(delete("/api/bills/1"))
            .andExpect(status().isNoContent());

        verify(billService, times(1)).deleteBill(1L);
    }

    @Test
    void deleteBill_returns404_whenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Bill", 99L))
            .when(billService)
            .deleteBill(99L);

        mockMvc
            .perform(delete("/api/bills/99"))
            .andExpect(status().isNotFound());
    }
}
