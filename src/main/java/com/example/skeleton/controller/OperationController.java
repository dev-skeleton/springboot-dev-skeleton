package com.example.skeleton.controller;

import com.example.skeleton.constant.RestfulApiVersion;
import com.example.skeleton.model.entity.OperationLog;
import com.example.skeleton.service.OperationLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(RestfulApiVersion.V1_OPERATION_LOG)
@Validated(Default.class)
@Tag(name = "OperationLog", description = "OperationLog interfaces")
public class OperationController {

    private final OperationLogService operationLogService;

    public OperationController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<OperationLog> List(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"yyyy-MM-dd HH:mm:ss"}) @NotNull Date start, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"yyyy-MM-dd HH:mm:ss"}) @NotNull Date end) {
        return operationLogService.List(start, end);
    }

    @PreAuthorize("hasRole(T(com.example.skeleton.constant.SecurityURP).ADMIN_ROLE)")
    @RequestMapping(method = RequestMethod.DELETE)
    @com.example.skeleton.annotation.OperationLog(description = "删除操作记录", detailParam = {"start", "end"})
    public void DeleteByTimeRange(@RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"yyyy-MM-dd HH:mm:ss"}) Date start, @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = {"yyyy-MM-dd HH:mm:ss"}) Date end) {
        operationLogService.DeleteByTimeRange(start, end);
    }
}
