package com.project.iam.controller;

import com.project.iam.enumerations.Roles;
import com.project.iam.model.User;
import com.project.iam.service.RoleService;
import com.project.iam.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    //                                          CREATE
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    //                                          GET
    @GetMapping("/by-{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping(path = "/by-username", params = "username")
    public User getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping(path = "/by-email", params = "email")
    public User getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/activeUsers")
    public List<User> getAllActiveUsers() {
        return userService.getAllActiveUsers();
    }

    @GetMapping(path = "/by-role", params = "role")
    public List<User> getAllUsersByRole(@RequestParam String roleName) {
        if (!roleService.existsByName(roleName)) {
            throw new IllegalArgumentException("Role Not Found");
        }
        return userService.getAllUsersByRolename(roleName);
    }

    @GetMapping("/exists")
    public boolean existsUserByUsername(@RequestParam String username) {
        return userService.existsByUsername(username);
    }

    //                                          UPDATE
    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @RequestParam User user) {
        return userService.updateUser(id, user);
    }


    //                                          DELETE
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping(path = "/delete-by-username", params = "username")
    public void deleteUserByUsername(@RequestParam String username) {
        userService.deleteUserByUsername(username);
    }

    //                                          ROLES

    @PutMapping("/{userId}/roles/{roleId}")
    public void addRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userService.addRoleToUser(userId, roleId);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public void removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        userService.removeRoleFromUser(userId, roleId);
    }
}

