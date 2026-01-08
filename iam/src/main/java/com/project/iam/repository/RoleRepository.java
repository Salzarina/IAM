package com.project.iam.repository;

import com.project.iam.model.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
    Optional<Role> findWithPermissionsByName(String name);

    boolean existsByName(String name);

    @EntityGraph(attributePaths = "permissions")
    List<Role> findAllByNameIn(Set<String> names);
}
