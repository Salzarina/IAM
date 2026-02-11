package com.project.iam.controller;

import com.project.iam.model.Permission;
import com.project.iam.model.Role;
import com.project.iam.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public Role createRole(@RequestParam String roleName) {
        return roleService.createRole(roleName);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/{name}")
    public Role getRoleByName(@RequestParam String name) {
        return roleService.getRoleByName(name);
    }

    @PutMapping("/update/{id}")
    public Role updateRole(
            @PathVariable Long id,
            @RequestBody Role role
    ) {
        return roleService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleById(@PathVariable Long id) {
        roleService.deleteRoleById(id);
    }

    @DeleteMapping("/{name}")
    public void deleteRoleByName(@RequestParam String name) {
        roleService.deleteRoleByName(name);
    }

    @PutMapping("/{roleId}/permissions")
    public void addPermissionToRole(
            @PathVariable Long roleId,
            @RequestParam Long permissionId
    ) {
        roleService.addPermission(roleId, permissionId);
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public void removePermissionFromRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId
    ) {
        roleService.deletePermission(roleId, permissionId);
    }

    @GetMapping("/{roleId}/permissions")
    public List<Permission> getAllPermissionsInRole(@PathVariable Long roleId) {
        return roleService.getAllPermissionsInRole(getRoleById(roleId));
    }
}

