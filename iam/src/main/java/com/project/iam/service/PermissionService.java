package com.project.iam.service;

import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.Permission;
import com.project.iam.model.Role;
import com.project.iam.repository.PermissionRepository;
import com.project.iam.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final AuditLogService auditLogService;
    private final RoleRepository roleRepository;

    public PermissionService(PermissionRepository permissionRepository, AuditLogService auditLogService, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.auditLogService = auditLogService;
        this.roleRepository = roleRepository;
    }

    public Permission createPermission(String permissionName) {
        if (permissionRepository.existsByName(permissionName)) {
            throw new IllegalArgumentException(
                    "Permission with name " + permissionName + " already exists"
            );
        }

        Permission permission = Permission.builder()
                .name(permissionName)
                .build();

        Permission savedPermission = permissionRepository.save(permission);

        auditLogService.logSystemAction(
                AuditAction.PERMISSION_CREATE,
                "Permission " + permissionName + " has been created"
        );

        return savedPermission;
    }

    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Permission with id " + id + " does not exist"));
    }

    public Permission getPermissionByName(String name) {
        return permissionRepository.findByName(name).
                orElseThrow(() -> new IllegalArgumentException("Permission with name " + name + " does not exist"));
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public List<Permission> getAllPermissionsByRole(String name) {
        if (!roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("Role with name " + name + " does not exist");
        }
        return permissionRepository.findAllByRolesName(name);
    }

    public Permission updatePermission(Long id, Permission newPermission) {

        Permission existingPermission = getPermissionById(id);

        if (!newPermission.getName().equals(existingPermission.getName()) && permissionRepository.existsByName(newPermission.getName())) {
            throw new IllegalArgumentException("Permission with name " + newPermission.getName() + " already exists");
        }

        existingPermission.setName(newPermission.getName());

        auditLogService.logSystemAction(AuditAction.PERMISSION_UPDATE, "Permission " + newPermission.getName() + " has been updated");

        return permissionRepository.save(existingPermission);
    }

    public void deletePermissionById(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new IllegalArgumentException("Permission with id " + id + " does not exist");
        }

        permissionRepository.deleteById(id);

        auditLogService.logSystemAction(AuditAction.PERMISSION_DELETE, "Permission " + id + " has been deleted");
    }

    @Transactional
    public void deletePermissionByName(String name) {
        if (!permissionRepository.existsByName(name)) {
            throw new IllegalArgumentException("Permission with name " + name + " does not exist");
        }

        permissionRepository.deleteByName(name);

        auditLogService.logSystemAction(AuditAction.PERMISSION_DELETE, "Permission " + name + " has been deleted");
    }

}
