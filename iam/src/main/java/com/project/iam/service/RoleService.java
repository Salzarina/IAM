package com.project.iam.service;

import com.project.iam.model.Permission;
import com.project.iam.model.Role;
import com.project.iam.repository.PermissionRepository;
import com.project.iam.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getRoleName())) {
            throw new IllegalArgumentException("Role already exists");
        }
        return roleRepository.save(role);
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Role Not Found"));
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName).
                orElseThrow(() -> new IllegalArgumentException("Role Not Found"));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role updateRole(Long id, Role role) {

        Role existingRole = getRoleById(id);

        if (!role.getRoleName().equals(existingRole.getRoleName()) && roleRepository.existsByName(role.getRoleName())) {
            throw new IllegalArgumentException("Role already exists");
        }

        existingRole.setRoleName(role.getRoleName());

        return roleRepository.save(existingRole);
    }

    public void deleteRoleById(Long id) {
        if(!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role Not Found");
        }
        roleRepository.deleteById(id);
    }

    public void deleteRoleByName(String roleName) {
        if(!roleRepository.existsByName(roleName)) {
            throw new IllegalArgumentException("Role Not Found");
        }
        roleRepository.deleteByRoleName(roleName);
    }

    public void addPermission(Role role, Permission permission) {
        role.getPermissions().add(permission);
        roleRepository.save(role);
    }

    public void deletePermission(Role role, Permission permission) {
        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }

    public List<Permission> getAllPermissionsInRole(Role role) {
        Role foundRole = roleRepository.findWithPermissionsByName(role.getRoleName())
                .orElseThrow(() -> new IllegalArgumentException("There are no permissions in the role"));

        return new ArrayList<>(foundRole.getPermissions() != null ? foundRole.getPermissions() : Collections.emptyList());
    }
}
