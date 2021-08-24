package com.example.skeleton.controller;

import com.example.skeleton.constant.RestfulApiVersion;
import com.example.skeleton.model.entity.Permission;
import com.example.skeleton.service.PermissionSvc;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

@RestController
@RequestMapping(RestfulApiVersion.V1_PERMISSION)
@Validated(Default.class)
@Tag(name = "Permission", description = "Permission module interfaces")
public class PermissionController {
    private final PermissionSvc permissionSvc;

    public PermissionController(PermissionSvc permissionSvc) {
        this.permissionSvc = permissionSvc;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Permission> List() {
        return permissionSvc.ListAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Permission Get(@PathVariable @NotNull Long id) {
        return permissionSvc.GetByID(id);
    }

}
