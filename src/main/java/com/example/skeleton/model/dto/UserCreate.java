package com.example.skeleton.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreate {
    @Schema(required = true, minLength = 3, maxLength = 16, pattern = "[a-zA-Z0-9_-]+")
    private String name;
    @Schema(required = true, minLength = 8, maxLength = 32)
    private String password;

    private Set<Long> roles;
}
