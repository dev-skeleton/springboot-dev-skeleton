package com.example.skeleton.exception;


import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class WebAppException extends CustomRuntimeException {
    private final Integer code;
    private final String msg;
    private final LocalDateTime occurredAt;

    public WebAppException(@NotNull Integer code, @NotBlank String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.occurredAt = LocalDateTime.now();
    }

}
