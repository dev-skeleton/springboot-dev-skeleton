package com.example.skeleton.service;

import com.example.skeleton.model.entity.OperationLog;
import com.example.skeleton.repo.OperationLogRepo;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

@Service
@Validated(Default.class)
public class OperationLogService extends CrudSvc<OperationLog, Long> {

    private final OperationLogRepo operationLogRepo;

    public OperationLogService(OperationLogRepo operationLogRepo) {
        super(operationLogRepo, operationLogRepo);
        this.operationLogRepo = operationLogRepo;
    }

    public List<OperationLog> List(@NotNull Date start, @NotNull Date end) {
        return operationLogRepo.findAllByTimeBetween(start, end);
    }

    public void Put(@NotNull OperationLog record) {
        operationLogRepo.save(record);
    }

    public void DeleteByTimeRange(@NotNull Date start, @NotNull Date end) {
        operationLogRepo.deleteByTimeBetween(start, end);
    }

}
