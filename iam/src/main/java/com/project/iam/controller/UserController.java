package com.project.iam.controller;

import com.project.iam.enumerations.Roles;
import com.project.iam.model.User;
import com.project.iam.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class UserController {

    private UserService userService;

    public User createUser(User user) {
        return userService.create(user);
    }

    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<User> getAllUsersByRolename(Roles role) {
        return userService.getAllUsersByRolename(role);
    }

    public User updateUser(Long id, User user) {
        return userService.updateUser(id, user);
    }

    public void deleteUserById(Long id) {
        userService.deleteUserById(id);
    }

    public void deleteUserByUsername(String username) {
        userService.deleteUserByUsername(username);
    }

    public boolean existsUserByUsername(String username) {
        return userService.existsByUsername(username);
    }

}

