package com.example.skeleton.controller;

import com.example.skeleton.constant.RestfulApiVersion;
import com.example.skeleton.util.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(RestfulApiVersion.V1_SAMPLE + "/rbac")
@Tag(name = "RBAC Sample", description = "Check if RABC role and permission works, switch to different user to do the test")
public class RbacSampleController {

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<?> HasAdminRole() {
        return Api.SimpleJsonResp(HttpStatus.OK, List.of(Pair.of("test", "ok")));
    }

    @PreAuthorize("hasRole('ops')")
    @RequestMapping(value = "/ops", method = RequestMethod.GET)
    public ResponseEntity<?> HasOpsRole() {
        return Api.SimpleJsonResp(HttpStatus.OK, List.of(Pair.of("test", "ok")));
    }

    @PreAuthorize("hasRole('common')")
    @RequestMapping(value = "/common", method = RequestMethod.GET)
    public ResponseEntity<?> HasCommonRole() {
        return Api.SimpleJsonResp(HttpStatus.OK, List.of(Pair.of("test", "ok")));
    }

    @PreAuthorize("hasRole('audit')")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public ResponseEntity<?> HasAuditRole() {
        return Api.SimpleJsonResp(HttpStatus.OK, List.of(Pair.of("test", "ok")));
    }

    @PreAuthorize("hasAnyAuthority(T(com.example.skeleton.constant.SecurityURP).RBAC_SAMPLE)")
    @RequestMapping(value = "/test-permission", method = RequestMethod.GET)
    public ResponseEntity<?> HasTestAuthority() {
        return Api.SimpleJsonResp(HttpStatus.OK, List.of(Pair.of("test", "ok")));
    }
}
