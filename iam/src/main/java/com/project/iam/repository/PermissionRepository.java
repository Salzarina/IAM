package com.project.iam.repository;

import com.project.iam.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(String name);

    boolean existsByName(String name);

    List<Permission> findAllByNameIn(Set<String> names);
    List<Permission> findAllByRolesName(String name);
}
