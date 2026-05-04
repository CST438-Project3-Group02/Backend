package com.roomie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class InviteExpiredException extends RuntimeException {

    public InviteExpiredException(String inviteCode) {
        super("Invite code " + inviteCode + " has expired");
    }
}