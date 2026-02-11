package com.project.iam.service;

import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.Permission;
import com.project.iam.model.Role;
import com.project.iam.repository.PermissionRepository;
import com.project.iam.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final AuditLogService auditLogService;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, AuditLogService auditLogService, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.auditLogService = auditLogService;
        this.permissionRepository = permissionRepository;
    }

    public Role createRole(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            throw new IllegalArgumentException("Role already exists");
        }

        Role role = Role.builder()
                .name(roleName)
                .permissions(new HashSet<>())
                .build();

        auditLogService.logSystemAction(AuditAction.ROLE_CREATE, "Role has been created");

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

        if (!role.getName().equals(existingRole.getName()) && roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Role already exists");
        }

        existingRole.setName(role.getName());

        auditLogService.logSystemAction(AuditAction.ROLE_UPDATE, "Role has been updated");

        return roleRepository.save(existingRole);
    }

    public void deleteRoleById(Long id) {
        if(!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role Not Found");
        }

        roleRepository.deleteById(id);

        auditLogService.logSystemAction(AuditAction.ROLE_DELETE, "Role has been deleted");
    }

    @Transactional
    public void deleteRoleByName(String roleName) {
        if(!roleRepository.existsByName(roleName)) {
            throw new IllegalArgumentException("Role Not Found");
        }


        auditLogService.logSystemAction(AuditAction.ROLE_DELETE, "Role has been deleted");
    }

    public void addPermission(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

        role.getPermissions().add(permission);
        roleRepository.save(role);
    }

    public void deletePermission(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }

    public List<Permission> getAllPermissionsInRole(Role role) {
        Role foundRole = roleRepository.findWithPermissionsByName(role.getName())
                .orElseThrow(() -> new IllegalArgumentException("There are no permissions in the role"));

        return new ArrayList<>(foundRole.getPermissions() != null ? foundRole.getPermissions() : Collections.emptyList());
    }

    public boolean existsByName(String roleName) {
        return roleRepository.existsByName(roleName);
    }
}
