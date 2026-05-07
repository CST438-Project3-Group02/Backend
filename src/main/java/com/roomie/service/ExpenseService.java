package com.roomie.service;

import com.roomie.dto.ExpenseDTO;
import com.roomie.entity.Bill;
import com.roomie.entity.Expense;
import com.roomie.entity.Household;
import com.roomie.entity.Profile;
import com.roomie.entity.ProfileHousehold;
import com.roomie.enums.ActivityType;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.BillRepository;
import com.roomie.repository.ExpenseRepository;
import com.roomie.repository.HouseholdRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final HouseholdRepository householdRepository;
    private final BillRepository billRepository;
    private final ActivityEventService activityEventService;

    public ExpenseService(
        ExpenseRepository expenseRepository,
        HouseholdRepository householdRepository,
        BillRepository billRepository,
        ActivityEventService activityEventService
    ) {
        this.expenseRepository = expenseRepository;
        this.householdRepository = householdRepository;
        this.billRepository = billRepository;
        this.activityEventService = activityEventService;
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

    private void recalculateBills(Expense expense, List<Profile> members) {
        billRepository.deleteAll(
            billRepository.findByExpense_ExpenseId(expense.getExpenseId())
        );
        if (members.isEmpty()) return;

        double splitAmount = 100.0 / members.size(); // percentage
        expense.setSplitAmount(splitAmount);
        expenseRepository.save(expense);

        double billAmount = expense.getAmount() * (splitAmount / 100.0);
        for (Profile member : members) {
            Bill bill = new Bill(
                expense.getDescription(),
                billAmount,
                false,
                expense.getPaidByDate(),
                member
            );
            bill.setExpense(expense);
            billRepository.save(bill);
        }
    }

    private List<Profile> getHouseholdMembers(Household household) {
        Household householdWithMembers = householdRepository
            .findHouseholdWithProfiles(household.getHouseholdId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Household",
                    household.getHouseholdId()
                )
            );

        if (
            householdWithMembers.getProfileHouseholds() == null
        ) return new ArrayList<>();
        return householdWithMembers
            .getProfileHouseholds()
            .stream()
            .map(ProfileHousehold::getProfile)
            .collect(Collectors.toList());
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
        Instant paidByDate,
        List<Profile> members
    ) {
        Household household = householdRepository
            .findHouseholdWithProfiles(householdId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Household", householdId)
            );

        // List<Profile> members =
        //     household.getProfileHouseholds() == null
        //         ? new ArrayList<>()
        //         : household
        //               .getProfileHouseholds()
        //               .stream()
        //               .map(ProfileHousehold::getProfile)
        //               .collect(Collectors.toList());

        // double splitAmount = members.isEmpty()
        //     ? amount
        //     : amount / members.size();
        double splitAmount = members.isEmpty() ? 100.0 : 100.0 / members.size();

        Expense expense = new Expense(
            description,
            amount,
            splitAmount,
            false,
            paidByDate
        );
        expense.setHousehold(household);
        Expense saved = expenseRepository.save(expense);

        // ✅ early return here — before bill generation
        if (members.isEmpty()) {
            return toDTO(saved);
        }
        double billAmount = amount * (splitAmount / 100.0);
        for (Profile member : members) {
            Bill bill = new Bill(
                description,
                billAmount,
                false,
                paidByDate,
                member
            );
            bill.setExpense(saved);
            billRepository.save(bill);
        }

        activityEventService.log(
            members.get(0).getProfileId(),
            householdId,
            ActivityType.EXPENSE_CREATED,
            false
        );

        return toDTO(saved);
    }

    public void updateRentExpense(Household household, double newAmount) {
        List<Expense> householdExpenses =
            expenseRepository.findByHousehold_HouseholdId(
                household.getHouseholdId()
            );

        Expense rentExpense = householdExpenses
            .stream()
            .filter(e -> e.getDescription().equals("Rent"))
            .findFirst()
            .orElse(null);

        List<Profile> members = getHouseholdMembers(household);

        if (rentExpense == null) {
            createExpense(
                household.getHouseholdId(),
                "Rent",
                newAmount,
                null,
                members
            );
            return;
        }

        // rent expense exists — update amount and recalculate bills
        rentExpense.setAmount(newAmount);
        expenseRepository.save(rentExpense);
        recalculateBills(rentExpense, members);
    }

    public void recalculateAllHouseholdExpenses(Household household) {
        List<Profile> members = getHouseholdMembers(household);

        List<Expense> expenses = expenseRepository.findByHousehold_HouseholdId(
            household.getHouseholdId()
        );

        expenses
            .stream()
            .filter(e -> !e.getPaid())
            .forEach(e -> recalculateBills(e, members));
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
