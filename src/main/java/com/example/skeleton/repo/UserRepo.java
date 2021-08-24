package com.example.skeleton.repo;

import com.example.skeleton.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findUserByNameEquals(String name);
}
