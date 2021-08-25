package com.example.skeleton.component.apprunner;

import com.example.skeleton.model.entity.User;
import com.example.skeleton.service.RoleSvc;
import com.example.skeleton.service.UserSvc;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Set;

@Profile("dev")
@Component
public class DevURP implements ApplicationRunner {
    private final UserSvc userSvc;
    private final RoleSvc roleSvc;

    public DevURP(UserSvc userSvc, RoleSvc roleSvc) {
        this.userSvc = userSvc;
        this.roleSvc = roleSvc;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        var admin = roleSvc.FindByName("admin");
        var common = roleSvc.FindByName("common");
        var audit = roleSvc.FindByName("audit");
        var ops = roleSvc.FindByName("ops");

        var defaultUser = User.builder().name("dev").password("dev_pass_wd").grantedRoles(Set.of(admin)).passwordUpdateTimestamp(new Date()).locked(false).build();
        userSvc.Merge(defaultUser);
        var auditUser = User.builder().name("auditDev").password("audit_pass_wd").grantedRoles(Set.of(audit)).passwordUpdateTimestamp(new Date()).locked(false).build();
        userSvc.Merge(auditUser);
        var opsUser = User.builder().name("opsDev").password("ops_pass_wd").grantedRoles(Set.of(ops)).passwordUpdateTimestamp(new Date()).locked(false).build();
        userSvc.Merge(opsUser);
        var commonUser = User.builder().name("commonDev").password("audit_pass_wd").grantedRoles(Set.of(common)).passwordUpdateTimestamp(new Date()).locked(false).build();
        userSvc.Merge(commonUser);
    }
}
