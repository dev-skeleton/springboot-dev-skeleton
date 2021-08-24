package com.example.skeleton.exception;

import org.springframework.http.HttpStatus;

public class ClientSideException extends WebAppException {

    public ClientSideException(Integer code, String msg) {
        super(code, msg);
    }

    static public ClientSideException BadRequest(String msg) {
        return new ClientSideException(HttpStatus.BAD_REQUEST.value(), msg);
    }

    static public ClientSideException NotFound(String msg) {
        return new ClientSideException(HttpStatus.NOT_FOUND.value(), msg);
    }

    static public ClientSideException Forbidden(String msg) {
        return new ClientSideException(HttpStatus.FORBIDDEN.value(), msg);
    }

}
