package com.example.skeleton.component.apprunner;

import com.example.skeleton.constant.SecurityURP;
import com.example.skeleton.service.PermissionSvc;
import com.example.skeleton.service.RoleSvc;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultURP implements ApplicationRunner {

    private final RoleSvc roleSvc;
    private final PermissionSvc permissionSvc;

    public DefaultURP(RoleSvc roleSvc, PermissionSvc permissionSvc) {
        this.roleSvc = roleSvc;
        this.permissionSvc = permissionSvc;
    }

    @Override
    public void run(ApplicationArguments args) {
        SecurityURP.Permissions.forEach(permissionSvc::Merge);
        SecurityURP.Roles.forEach(roleSvc::Merge);
    }
}
