package com.example.skeleton.exception;

import org.springframework.http.HttpStatus;

public class ServerSideException extends WebAppException {
    public ServerSideException(Integer code, String msg) {
        super(code, msg);
    }

    static public ServerSideException InternalError(String msg) {
        return new ServerSideException(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }
}
