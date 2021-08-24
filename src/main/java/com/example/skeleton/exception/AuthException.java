package com.example.skeleton.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends ClientSideException {

    public AuthException(String msg) {
        super(HttpStatus.UNAUTHORIZED.value(), msg);
    }

    static public AuthException WrongUserOrPassword() {
        return new AuthException("wrong user or password");
    }

    static public AuthException AccountLocked() {
        return new AuthException("account locked");
    }

    static public AuthException PasswordExpired() {
        return new AuthException("password expired");
    }
}
