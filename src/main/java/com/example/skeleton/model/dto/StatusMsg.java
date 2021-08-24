package com.example.skeleton.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class StatusMsg {
    private final Integer code;
    private final String msg;
    private final LocalDateTime occurred_at;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object Details;
}
