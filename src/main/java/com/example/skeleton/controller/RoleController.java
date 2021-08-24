package com.example.skeleton.controller;

import com.example.skeleton.annotation.OperationLog;
import com.example.skeleton.constant.RestfulApiVersion;
import com.example.skeleton.model.dto.RoleCreate;
import com.example.skeleton.model.dto.RoleUpdate;
import com.example.skeleton.model.entity.Permission;
import com.example.skeleton.model.entity.Role;
import com.example.skeleton.service.RoleSvc;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(RestfulApiVersion.V1_ROLE)
@Tag(name = "Role", description = "Role module interfaces")
public class RoleController {

    private final RoleSvc roleSvc;

    public RoleController(RoleSvc roleSvc) {
        this.roleSvc = roleSvc;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Role> List() {
        return roleSvc.ListAll();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @Validated
    @PreAuthorize("hasRole(T(com.example.skeleton.constant.SecurityURP).ADMIN_ROLE)")
    @OperationLog(description = "删除角色", entityParam = "id")
    public void Delete(@PathVariable @NotNull Long id) {
        roleSvc.DeleteById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @Validated
    @PreAuthorize("hasRole(T(com.example.skeleton.constant.SecurityURP).ADMIN_ROLE)")
    @OperationLog(description = "创建角色", entityParam = "roleCreate.name", detailParam = "roleCreate")
    public void Create(@RequestBody @NotNull RoleCreate roleCreate) {
        roleSvc.Create(Role.builder().name(roleCreate.getName()).permissions(roleCreate.getPermissions().stream().map(id -> Permission.builder().id(id).build()).collect(Collectors.toSet())).build());
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Validated
    @PreAuthorize("hasRole(T(com.example.skeleton.constant.SecurityURP).ADMIN_ROLE)")
    @OperationLog(description = "更新角色", entityParam = "roleUpdate.roleID", detailParam = "roleUpdate")
    public void Update(@RequestBody RoleUpdate roleUpdate) {
        roleSvc.Merge(Role.builder().id(roleUpdate.getRoleID()).permissions(roleUpdate.getPermissions().stream().map(id -> Permission.builder().id(id).build()).collect(Collectors.toSet())).build());
    }

}
