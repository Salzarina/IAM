package com.project.iam.controller;

import com.project.iam.model.Permission;
import com.project.iam.model.Role;
import com.project.iam.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public Permission createPermission(@RequestParam String permissionName) {
        return permissionService.createPermission(permissionName);
    }

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable Long id) {
        return permissionService.getPermissionById(id);
    }

    @GetMapping("/by-name")
    public Permission getPermissionByName(@RequestParam String name) {
        return permissionService.getPermissionByName(name);
    }

    @GetMapping("/by-role")
    public List<Permission> getAllPermissionsByRole(String roleName) { return permissionService.getAllPermissionsByRole(roleName); }

    @PutMapping("/update/{id}")
    public Permission updatePermission(
            @PathVariable Long id,
            @RequestBody Permission permission
    ) {
        return permissionService.updatePermission(id, permission);
    }

    @DeleteMapping("/{id}")
    public void deletePermissionById(@PathVariable Long id) {
        permissionService.deletePermissionById(id);
    }

    @DeleteMapping("/by-name")
    public void deletePermissionByName(@RequestParam String name) {
        permissionService.deletePermissionByName(name);
    }
}

