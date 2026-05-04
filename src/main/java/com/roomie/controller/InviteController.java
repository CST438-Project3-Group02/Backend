package com.roomie.controller;

import com.roomie.dto.InviteDTO;
import com.roomie.entity.Chore;
import com.roomie.entity.Invite;
import com.roomie.service.InviteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invite")
public class InviteController {

    private final InviteService inviteService;

    public InviteController(
            InviteService inviteService
    ) {
        this.inviteService = inviteService;
    }

    @PostMapping
    public ResponseEntity<InviteDTO> createInvite(
            @RequestBody Map<String, Object> payload
    ) {
        Long profileId = Long.valueOf(payload.get("profileId").toString());
        Long householdId = Long.valueOf(payload.get("householdId").toString());

        InviteDTO created = inviteService.createInvite(profileId, householdId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<InviteDTO> getInviteDetails(
            @RequestParam String inviteCode
    ) {
        return ResponseEntity.ok(inviteService.getInvite(inviteCode));
    }


}
