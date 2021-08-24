package com.example.skeleton.repo;

import com.example.skeleton.model.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface OperationLogRepo extends JpaRepository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog> {
    List<OperationLog> findAllByTimeBetween(Date start, Date end);

    @Modifying
    @Transactional
    void deleteByTimeBetween(Date start, Date end);
}
