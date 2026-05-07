package com.roomie.controller;

import com.roomie.dto.ExpenseDTO;
import com.roomie.entity.Expense;
import com.roomie.service.ExpenseService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // GET /api/expenses
    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    // GET /api/expenses/1
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    // GET /api/expenses/1/bills
    @GetMapping("/{id}/bills")
    public ResponseEntity<ExpenseDTO> getExpenseWithBills(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(expenseService.getExpenseWithBills(id));
    }

    // GET /api/expenses/household/1
    @GetMapping("/household/{householdId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByHousehold(
        @PathVariable Long householdId
    ) {
        return ResponseEntity.ok(
            expenseService.getExpensesByHousehold(householdId)
        );
    }

    // GET /api/expenses/household/1/full
    @GetMapping("/household/{householdId}/full")
    public ResponseEntity<List<ExpenseDTO>> getExpensesWithBillsByHousehold(
        @PathVariable Long householdId
    ) {
        return ResponseEntity.ok(
            expenseService.getExpensesWithBillsByHousehold(householdId)
        );
    }

    // GET /api/expenses/household/1/status?paid=false
    @GetMapping("/household/{householdId}/status")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByHouseholdAndStatus(
        @PathVariable Long householdId,
        @RequestParam Boolean paid
    ) {
        return ResponseEntity.ok(
            expenseService.getExpensesByHouseholdAndStatus(householdId, paid)
        );
    }

    // POST /api/expenses
    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(
        @RequestBody Map<String, Object> payload
    ) {
        Long householdId = Long.valueOf(payload.get("householdId").toString());
        String description = payload.get("description").toString();
        Double amount = Double.valueOf(payload.get("amount").toString());
        Instant paidByDate = payload.containsKey("paidByDate")
            ? Instant.parse(payload.get("paidByDate").toString())
            : null;

        return ResponseEntity.status(HttpStatus.CREATED).body(
            expenseService.createExpense(
                householdId,
                description,
                amount,
                paidByDate,
                null
            )
        );
    }

    // PUT /api/expenses/1
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(
        @PathVariable Long id,
        @RequestBody Expense expense
    ) {
        return ResponseEntity.ok(expenseService.updateExpense(id, expense));
    }

    // PATCH /api/expenses/1/paid
    @PatchMapping("/{id}/paid")
    public ResponseEntity<ExpenseDTO> markPaid(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.markPaid(id));
    }

    // DELETE /api/expenses/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
