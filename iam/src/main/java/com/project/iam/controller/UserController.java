package com.project.iam.controller;

import com.project.iam.enumerations.Roles;
import com.project.iam.model.User;
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

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(path = "/by-role", params = "role")
    public List<User> getAllUsersByRole(@RequestParam Roles role) {
        return userService.getAllUsersByRolename(role);
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

}

