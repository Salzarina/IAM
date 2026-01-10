package com.project.iam.repository;

import com.project.iam.enumerations.Roles;
import com.project.iam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> findAllByRolesName(Roles roleName);
}
