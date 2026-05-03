package com.roomie.controller;

import com.roomie.dto.BillDTO;
import com.roomie.service.BillService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    // GET /api/bills/1
    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    // GET /api/bills/profile/1
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<BillDTO>> getBillsByProfile(
        @PathVariable Long profileId
    ) {
        return ResponseEntity.ok(billService.getBillsByProfile(profileId));
    }

    // GET /api/bills/profile/1/status?paid=false
    @GetMapping("/profile/{profileId}/status")
    public ResponseEntity<List<BillDTO>> getBillsByProfileAndStatus(
        @PathVariable Long profileId,
        @RequestParam Boolean paid
    ) {
        return ResponseEntity.ok(
            billService.getBillsByProfileAndStatus(profileId, paid)
        );
    }

    // GET /api/bills/expense/1
    @GetMapping("/expense/{expenseId}")
    public ResponseEntity<List<BillDTO>> getBillsByExpense(
        @PathVariable Long expenseId
    ) {
        return ResponseEntity.ok(billService.getBillsByExpense(expenseId));
    }

    // PATCH /api/bills/1/paid
    @PatchMapping("/{id}/paid")
    public ResponseEntity<BillDTO> markPaid(@PathVariable Long id) {
        return ResponseEntity.ok(billService.markPaid(id));
    }

    // DELETE /api/bills/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
