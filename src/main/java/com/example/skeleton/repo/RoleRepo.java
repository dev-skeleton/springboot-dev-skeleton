package com.example.skeleton.repo;

import com.example.skeleton.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepo extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Role findRoleByNameEquals(String name);
}
