package com.example.skeleton.exception;

import java.time.LocalDateTime;

public abstract class CustomRuntimeException extends RuntimeException {
    CustomRuntimeException(String msg) {
        super(msg);
    }

    public CustomRuntimeException CausedBy(Throwable t) {
        super.initCause(t);
        return this;
    }

    public abstract Integer getCode();

    public abstract String getMsg();

    public abstract LocalDateTime getOccurredAt();

}
