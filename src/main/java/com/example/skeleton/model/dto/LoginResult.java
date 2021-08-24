package com.example.skeleton.model.dto;


import com.example.skeleton.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {
    private String token;
    private User user;
}
