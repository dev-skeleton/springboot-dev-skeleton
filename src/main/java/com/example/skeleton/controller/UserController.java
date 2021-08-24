package com.example.skeleton.controller;

import com.example.skeleton.constant.RestfulApiVersion;
import com.example.skeleton.annotation.OperationLog;
import com.example.skeleton.model.dto.LoginInfo;
import com.example.skeleton.model.dto.LoginResult;
import com.example.skeleton.model.dto.UserCreate;
import com.example.skeleton.model.dto.UserUpdate;
import com.example.skeleton.model.entity.Role;
import com.example.skeleton.model.entity.User;
import com.example.skeleton.service.RoleSvc;
import com.example.skeleton.service.UserSvc;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(RestfulApiVersion.V1_USER)
@Validated(Default.class)
@Tag(name = "User", description = "User module and login interfaces")
public class UserController {
    private final UserSvc userSvc;
    private final RoleSvc roleSvc;

    public UserController(UserSvc userSvc, RoleSvc roleSvc) {
        this.userSvc = userSvc;
        this.roleSvc = roleSvc;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> ListAll() {
        return userSvc.ListAll();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @OperationLog(description = "登录用户", entityParam = "info.user")
    public LoginResult Login(@NotNull @RequestBody LoginInfo info) {
        return userSvc.login(info);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole(T(com.example.skeleton.constant.SecurityURP).ADMIN_ROLE)")
    @OperationLog(description = "删除用户", entityParam = "id")
    public void DeleteById(@NotNull @PathVariable Long id) {
        userSvc.DeleteById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole(T(com.example.skeleton.constant.SecurityURP).ADMIN_ROLE)")
    @OperationLog(description = "创建用户", entityParam = "userCreate.name", detailParam = "userCreate")
    public void CreateUser(@RequestBody @NotNull UserCreate userCreate) {
        var user = User.builder().name(userCreate.getName()).password(userCreate.getPassword()).grantedRoles(roleSvc.MapByID(userCreate.getRoles())).build();
        userSvc.Create(user);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole(T(com.example.skeleton.constant.SecurityURP).ADMIN_ROLE)")
    @OperationLog(description = "更新用户", entityParam = "userUpdate.id", detailParam = "userUpdate")
    public void UpdateUser(@RequestBody UserUpdate userUpdate) {
        var ub = User.builder().id(userUpdate.getId());
        if (userUpdate.getRoles() != null) {
            ub.grantedRoles(userUpdate.getRoles().stream().map(id -> Role.builder().id(id).build()).collect(Collectors.toSet()));
        }
        if (userUpdate.getPassword() != null) {
            ub.password(userUpdate.getPassword());
        }
        if (userUpdate.getLocked() != null) {
            ub.locked(userUpdate.getLocked());
        }
        userSvc.Merge(ub.build());
    }

}
