package com.project.iam.controller;

import com.project.iam.model.Permission;
import com.project.iam.model.Role;
import com.project.iam.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class PermissionController {

    private PermissionService permissionService;

    public Permission createPermission(Permission permission) {
        return permissionService.createPermission(permission);
    }

    public Permission getPermissionById(Long id) {
        return permissionService.getPermissionById(id);
    }

    public Permission getPermissionByName(String name) {
        return permissionService.getPermissionByName(name);
    }

    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    public List<Permission> getAllPermissionsByRole(Role role) {
        return permissionService.getAllPermissionsByRole(role);
    }

    public Permission updatePermission(Long id, Permission permission) {
        return permissionService.updatePermission(id, permission);
    }

    public void deletePermissionById(Long id) {
        permissionService.deletePermissionById(id);
    }

    public void deletePermissionByName(String name) {
        permissionService.deletePermissionByName(name);
    }

}
