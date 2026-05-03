package com.roomie.service;

import com.roomie.dto.ExpenseDTO;
import com.roomie.entity.Bill;
import com.roomie.entity.Expense;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.BillRepository;
import com.roomie.repository.ExpenseRepository;
import com.roomie.repository.HouseholdRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final HouseholdRepository householdRepository;
    private final BillRepository billRepository;

    public ExpenseService(
        ExpenseRepository expenseRepository,
        HouseholdRepository householdRepository,
        BillRepository billRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.householdRepository = householdRepository;
        this.billRepository = billRepository;
    }

    public ExpenseDTO toDTO(Expense expense) {
        return new ExpenseDTO(
            expense.getExpenseId(),
            expense.getDescription(),
            expense.getAmount(),
            expense.getSplitAmount(),
            expense.getPaid(),
            expense.getPaidByDate(),
            expense.getCreatedAt(),
            expense.getHousehold() != null
                ? expense.getHousehold().getHouseholdId()
                : null,
            expense.getHousehold() != null
                ? expense.getHousehold().getHouseholdName()
                : null
        );
    }

    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository
            .findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public ExpenseDTO getExpenseById(Long id) {
        Expense expense = expenseRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
        return toDTO(expense);
    }

    public ExpenseDTO getExpenseWithBills(Long id) {
        Expense expense = expenseRepository
            .findExpenseWithBills(id)
            .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
        ExpenseDTO dto = toDTO(expense);
        dto.setBills(expense.getBills());
        return dto;
    }

    public List<ExpenseDTO> getExpensesByHousehold(Long householdId) {
        return expenseRepository
            .findByHousehold_HouseholdId(householdId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<ExpenseDTO> getExpensesWithBillsByHousehold(Long householdId) {
        return expenseRepository
            .findHouseholdExpensesWithBills(householdId)
            .stream()
            .map(e -> {
                ExpenseDTO dto = toDTO(e);
                dto.setBills(e.getBills());
                return dto;
            })
            .collect(Collectors.toList());
    }

    public List<ExpenseDTO> getExpensesByHouseholdAndStatus(
        Long householdId,
        Boolean paid
    ) {
        return expenseRepository
            .findByHousehold_HouseholdIdAndPaid(householdId, paid)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    // core method — creates expense and auto-generates bills for each household member
    public ExpenseDTO createExpense(
        Long householdId,
        String description,
        Double amount,
        Instant paidByDate
    ) {
        Household household = householdRepository
            .findById(householdId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Household", householdId)
            );

        // get all profiles in the household
        List<Profile> members = household
            .getProfileHouseholds()
            .stream()
            .map(ProfileHousehold::getProfile)
            .collect(Collectors.toList());

        if (members.isEmpty()) {
            throw new RuntimeException(
                "Household has no members to split the expense with"
            );
        }

        // calculate split amount
        double splitAmount = amount / members.size();

        // create and save the expense
        Expense expense = new Expense(
            description,
            amount,
            splitAmount,
            false,
            paidByDate
        );
        expense.setHousehold(household);
        Expense saved = expenseRepository.save(expense);

        // auto-generate a bill for each member
        for (Profile member : members) {
            Bill bill = new Bill(
                description,
                splitAmount,
                false,
                paidByDate,
                member
            );
            bill.setExpense(saved);
            billRepository.save(bill);
        }

        return toDTO(saved);
    }

    public ExpenseDTO updateExpense(Long id, Expense updatedExpense) {
        Expense existing = expenseRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
        existing.setDescription(updatedExpense.getDescription());
        existing.setAmount(updatedExpense.getAmount());
        existing.setSplitAmount(updatedExpense.getSplitAmount());
        existing.setPaid(updatedExpense.getPaid());
        existing.setPaidByDate(updatedExpense.getPaidByDate());
        return toDTO(expenseRepository.save(existing));
    }

    public ExpenseDTO markPaid(Long id) {
        Expense existing = expenseRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
        existing.setPaid(true);
        return toDTO(expenseRepository.save(existing));
    }

    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense", id);
        }
        expenseRepository.deleteById(id);
    }
}
