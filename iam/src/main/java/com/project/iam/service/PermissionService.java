package com.project.iam.service;

import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.Permission;
import com.project.iam.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final AuditLogService auditLogService;

    public PermissionService(PermissionRepository permissionRepository, AuditLogService auditLogService) {
        this.permissionRepository = permissionRepository;
        this.auditLogService = auditLogService;
    }

    public Permission createPermission(Permission permission) {
        if (permissionRepository.existsByName(permission.getName())) {
            throw new IllegalArgumentException("Permission with name " + permission.getName() + " already exists");
        }

        auditLogService.logSystemAction(AuditAction.PERMISSION_CREATE, "Permission " + permission.getName() + " has been created");

        return permissionRepository.save(permission);
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

        auditLogService.logSystemAction(AuditAction.PERMISSION_DELETE, "Permission " + id + " has been deleted");

        permissionRepository.deleteById(id);
    }

    public void deletePermissionByName(String name) {
        if (!permissionRepository.existsByName(name)) {
            throw new IllegalArgumentException("Permission with name " + name + " does not exist");
        }

        auditLogService.logSystemAction(AuditAction.PERMISSION_DELETE, "Permission " + name + " has been deleted");

        permissionRepository.deleteByName(name);
    }

}
