package com.project.iam.service;

import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.AuthResponse;
import com.project.iam.model.LoginRequest;
import com.project.iam.model.RefreshToken;
import com.project.iam.model.User;
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

        User newUser = userService.getUserByUsername(loginRequest.getUsername());
        if ( newUser == null || !newUser.getPassword().equals(loginRequest.getPassword()) || !newUser.isActive() || newUser.isBlocked() ){
            auditLogService.logSystemAction(AuditAction.LOGIN_FAILURE, "Invalid username or password");
            throw new IllegalArgumentException("Invalid User");
        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(tokenService.generateAccessToken(newUser));
        authResponse.setRefreshToken(tokenService.generateRefreshToken(newUser));

        auditLogService.logUserAction(newUser, AuditAction.LOGIN_SUCCESS, "User " + loginRequest.getUsername() + " logged in successfully");

        return authResponse;
    }

    public void logout(RefreshToken refreshToken) {

        if (!tokenService.validateRefreshToken(refreshToken)) {
            auditLogService.logSystemAction(AuditAction.LOGOUT, "Invalid token");
            throw new IllegalArgumentException("Invalid refresh token");
        }

        User user = tokenService.getUserByToken(refreshToken);

        auditLogService.logUserAction(user, AuditAction.LOGOUT, "User " + user.getUsername());

        tokenService.revokeToken(user);

    }

    public AuthResponse refreshToken(RefreshToken refreshToken) {
        if (!tokenService.validateRefreshToken(refreshToken)) {
            auditLogService.logSystemAction(AuditAction.TOKEN_REFRESH_FAILED, "Invalid token");
            throw new IllegalArgumentException("Invalid token");
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(tokenService.generateAccessToken(tokenService.getUserByToken(refreshToken)));
        authResponse.setRefreshToken(tokenService.generateRefreshToken(tokenService.getUserByToken(refreshToken)));

        return authResponse;
    }

    public AuthResponse register(User user) {

        if (userService.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userService.create(user);

        String refreshToken = tokenService.generateRefreshToken(savedUser);
        String accessToken = tokenService.generateAccessToken(savedUser);

        auditLogService.logUserAction(
                savedUser,
                AuditAction.USER_REGISTER,
                "User registered"
        );

        return new AuthResponse(accessToken, refreshToken);
    }

}
