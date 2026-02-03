package com.project.iam.controller;

import com.project.iam.model.Permission;
import com.project.iam.model.Role;
import com.project.iam.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;

    public Role createRole(Role role) {
        return roleService.createRole(role);
    }

    public Role getRoleById(Long id) {
        return roleService.getRoleById(id);
    }

    public Role getRoleByName(String name) {
        return roleService.getRoleByName(name);
    }

    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    public Role updateRole(Long id, Role role) {
        return roleService.updateRole(id, role);
    }

    public void deleteRoleById(Long id) {
        roleService.deleteRoleById(id);
    }

    public void deleteRoleByName(String name) {
        roleService.deleteRoleByName(name);
    }

    public void addPermission(Role role, Permission permission) {
        roleService.addPermission(role, permission);
    }

    public void deletePermission(Role role, Permission permission) {
        roleService.deletePermission(role, permission);
    }

    public List<Permission> getAllPermissionsInRole(Role role) {
        return roleService.getAllPermissionsInRole(role);
    }

}
