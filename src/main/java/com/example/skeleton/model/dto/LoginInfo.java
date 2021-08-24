package com.example.skeleton.model.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {
    @Schema(required = true, minLength = 3, maxLength = 32, pattern = "[a-zA-Z0-9]+", defaultValue = "dev")
    @NotBlank
    private String user;

    @Schema(required = true, defaultValue = "dev_pass_wd")
    @NotBlank
    private String password;

    @Schema(required = true, defaultValue = "account")
    @NotBlank
    @Parameter(name = "登录类型，msg = 短信 / account = 账号密码")
    private String type;
}
