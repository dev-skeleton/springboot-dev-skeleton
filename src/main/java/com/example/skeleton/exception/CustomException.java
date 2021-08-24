package com.example.skeleton.exception;

public class CustomException extends Exception {
    CustomException(String msg) {
        super(msg);
    }

    public CustomException CausedBy(Throwable t) {
        super.initCause(t);
        return this;
    }

}
