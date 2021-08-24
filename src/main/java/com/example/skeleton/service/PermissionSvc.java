package com.example.skeleton.service;


import com.example.skeleton.model.entity.Permission;
import com.example.skeleton.repo.PermissionRepo;
import org.springframework.stereotype.Service;

@Service
public class PermissionSvc extends CrudSvc<Permission, Long> {

    public PermissionSvc(PermissionRepo permissionRepo) {
        super(permissionRepo, permissionRepo);
    }

}
