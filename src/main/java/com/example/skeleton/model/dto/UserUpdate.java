package com.example.skeleton.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdate {
    @Schema(required = true)
    private Long id;
    private Set<Long> roles;
    @Schema(minLength = 8, maxLength = 32)
    private String password;
    private Boolean locked;
}
