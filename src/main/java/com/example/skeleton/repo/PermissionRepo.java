package com.example.skeleton.repo;

import com.example.skeleton.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepo extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    Permission findPermissionByPhraseEquals(String p);
}
