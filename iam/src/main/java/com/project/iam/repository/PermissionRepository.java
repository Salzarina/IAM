package com.project.iam.repository;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.project.iam.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(String name);

    boolean existsByName(String name);

    List<Permission> findAllByNameIn(Set<String> names);
    @Query("""
        select p
        from Role r
        join r.permissions p
        where r.name = :name
    """)
    List<Permission> findAllByRolesName(String name);

    @Modifying
    @Query("delete from Permission p where p.name = :name")
    void deleteByName(@Param("name") String name);
}
