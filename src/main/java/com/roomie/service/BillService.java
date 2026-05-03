package com.roomie.service;

import com.roomie.dto.BillDTO;
import com.roomie.entity.Bill;
import com.roomie.exception.ResourceNotFoundException;
import com.roomie.repository.BillRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public BillDTO toDTO(Bill bill) {
        return new BillDTO(
            bill.getBillId(),
            bill.getDescription(),
            bill.getAmount(),
            bill.getPaid(),
            bill.getPayByDate(),
            bill.getCreatedAt(),
            bill.getProfile() != null ? bill.getProfile().getProfileId() : null,
            bill.getProfile() != null ? bill.getProfile().getName() : null,
            bill.getExpense() != null ? bill.getExpense().getExpenseId() : null
        );
    }

    public List<BillDTO> getBillsByProfile(Long profileId) {
        return billRepository
            .findByProfile_ProfileId(profileId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<BillDTO> getBillsByExpense(Long expenseId) {
        return billRepository
            .findByExpense_ExpenseId(expenseId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<BillDTO> getBillsByProfileAndStatus(
        Long profileId,
        Boolean paid
    ) {
        return billRepository
            .findByProfile_ProfileIdAndPaid(profileId, paid)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public BillDTO getBillById(Long id) {
        Bill bill = billRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bill", id));
        return toDTO(bill);
    }

    public BillDTO markPaid(Long id) {
        Bill bill = billRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bill", id));
        bill.setPaid(true);
        return toDTO(billRepository.save(bill));
    }

    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bill", id);
        }
        billRepository.deleteById(id);
    }
}
