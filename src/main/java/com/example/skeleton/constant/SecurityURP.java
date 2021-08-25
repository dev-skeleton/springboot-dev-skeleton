package com.example.skeleton.constant;

import com.example.skeleton.model.entity.Permission;
import com.example.skeleton.model.entity.Role;
import com.example.skeleton.model.entity.User;

import java.util.List;
import java.util.Set;

public class SecurityURP {

    static public final String ADMIN_ROLE = "admin";
    static public final String OPS_ROLE = "ops";
    static public final String AUDIT_ROLE = "audit";
    static public final String COMMON_ROLE = "common";

    static public final String RBAC_SAMPLE = RestfulApiVersion.V1_SAMPLE + "/rbac";

    static public List<User> Users;
    static public List<Role> Roles;
    static public List<Permission> Permissions;

    static public Permission RBACTestPermission;

    static public Role AdminRole;
    static public Role OpsRole;
    static public Role AuditRole;
    static public Role CommonRole;

    static {
        RBACTestPermission = Permission.builder().phrase(RBAC_SAMPLE).description("RBAC test").build();

        AdminRole = Role.builder().name(ADMIN_ROLE).permissions(Set.of(RBACTestPermission)).build();
        OpsRole = Role.builder().name(OPS_ROLE).build();
        AuditRole = Role.builder().name(AUDIT_ROLE).build();
        CommonRole = Role.builder().name(COMMON_ROLE).build();

        Permissions = List.of(RBACTestPermission);
        Roles = List.of(AdminRole, AuditRole, OpsRole, CommonRole);

        Users = List.of(
                User.builder().name("admin").password("admin_pass_wd").build(),
                User.builder().name("ops").password("ops_pass_wd").build(),
                User.builder().name("audit").password("audit_pass_wd").build(),
                User.builder().name("common").password("common_pass_wd").build()
        );
    }
}
