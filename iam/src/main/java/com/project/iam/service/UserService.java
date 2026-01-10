package com.project.iam.service;

import com.project.iam.enumerations.Roles;
import com.project.iam.model.User;
import com.project.iam.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists!");
        }
        user.setActive(true);
        user.setBlocked(false);

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersByRolename(Roles role) {
        return userRepository.findAllByRolesName(role);
    }

    public User updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
                userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new IllegalArgumentException("Username already exists!");
        }
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setBlocked(updatedUser.isBlocked());
        existingUser.setActive(updatedUser.isActive());

        return userRepository.save(existingUser);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found!");
        }
        userRepository.deleteById(id);
    }

}
