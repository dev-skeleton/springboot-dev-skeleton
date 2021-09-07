package com.example.skeleton.service;

import com.example.skeleton.exception.ClientSideException;
import com.example.skeleton.model.entity.Role;
import com.example.skeleton.repo.RoleRepo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleSvc extends CrudSvc<Role, Long> {
    private final RoleRepo roleRepo;
    private final PermissionSvc permissionSvc;

    public RoleSvc(RoleRepo repo, PermissionSvc permissionSvc) {
        super(repo, repo);
        roleRepo = repo;
        this.permissionSvc = permissionSvc;
    }

    @Validated
    public @NotNull Role FindByName(@NotBlank String name) {
        var role = roleRepo.findRoleByNameEquals(name);
        if (role == null) {
            throw ClientSideException.NotFound(String.format("role [%s] not found", name));
        }
        return role;
    }

    @Override
    public void Merge(Role that) {
        if (that.getPermissions() != null) {
            that.setPermissions(that.getPermissions().stream().map(permissionSvc::Refresh).collect(Collectors.toSet()));
        }
        super.Merge(that);
    }

    @Validated
    public @NotNull Set<Role> MapByID(Set<Long> id) {
        if (id == null) {
            return new HashSet<>();
        }
        return id.stream().map(this::GetByID).collect(Collectors.toSet());
    }

}
