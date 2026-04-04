package com.project.iam.service;

import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuditLogService auditLogService;
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuditLogService auditLogService,
            UserService userService,
            TokenService tokenService,
            PasswordEncoder passwordEncoder
    ) {
        this.auditLogService = auditLogService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userService.getUserByUsername(loginRequest.getUsername());

        if (user == null) {
            auditLogService.logSystemAction(AuditAction.LOGIN_FAILURE, "User not found");
            throw new IllegalArgumentException("User not found");
        }

        if (!user.isActive() || user.isBlocked()) {
            auditLogService.logSystemAction(AuditAction.LOGIN_FAILURE, "User is blocked or inactive");
            throw new IllegalArgumentException("User is blocked or inactive");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            auditLogService.logSystemAction(AuditAction.LOGIN_FAILURE, "Bad credentials");
            throw new IllegalArgumentException("Invalid credentials");
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        auditLogService.logUserAction(user, AuditAction.LOGIN_SUCCESS, "User " + user.getUsername() + " logged in successfully");

        return new AuthResponse(accessToken, refreshToken);
    }

    public void logout(RefreshToken refreshToken) {
        if (!tokenService.validateRefreshToken(refreshToken)) {
            auditLogService.logSystemAction(AuditAction.LOGOUT, "Invalid token");
            throw new IllegalArgumentException("Invalid refresh token");
        }

        User user = tokenService.getUserByToken(refreshToken);

        tokenService.revokeToken(user);

        auditLogService.logUserAction(user, AuditAction.LOGOUT, "User " + user.getUsername() + " logged out successfully");
    }

    public AuthResponse refreshToken(RefreshToken refreshToken) {
        if (!tokenService.validateRefreshToken(refreshToken)) {
            auditLogService.logSystemAction(AuditAction.TOKEN_REFRESH_FAILED, "Invalid token");
            throw new IllegalArgumentException("Invalid token");
        }

        User user = tokenService.getUserByToken(refreshToken);

        tokenService.revokeToken(user);

        String accessToken = tokenService.generateAccessToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        auditLogService.logUserAction(user, AuditAction.TOKEN_REFRESH, "Token refreshed for user " + user.getUsername());

        return new AuthResponse(accessToken, newRefreshToken);
    }

    public AuthResponse register(RegRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .isBlocked(false)
                .build();

        User savedUser = userService.create(user);

        String accessToken = tokenService.generateAccessToken(savedUser);
        String refreshToken = tokenService.generateRefreshToken(savedUser);

        auditLogService.logUserAction(savedUser, AuditAction.USER_REGISTER, "User " + savedUser.getUsername() + " registered successfully");

        return new AuthResponse(accessToken, refreshToken);
    }
}