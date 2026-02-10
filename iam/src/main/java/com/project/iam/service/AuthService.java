package com.project.iam.service;

import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.*;
import com.project.iam.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.project.iam.enumerations.Roles.USER;


@Service
public class AuthService {

    private final AuditLogService auditLogService;
    private final UserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public AuthService(
            AuditLogService auditLogService,
            UserService userService,
            TokenService tokenService,
            PasswordEncoder passwordEncoder,
            RoleService roleService
    ) {
        this.auditLogService = auditLogService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public AuthResponse login(LoginRequest loginRequest) {

        User newUser = userService.getUserByUsername(loginRequest.getUsername());

        if (newUser == null) {
            auditLogService.logSystemAction(AuditAction.LOGIN_FAILURE, "User not found");
            throw new IllegalArgumentException("User not found");
        }
        if (!newUser.isActive() || newUser.isBlocked()) {
            auditLogService.logSystemAction(AuditAction.LOGIN_FAILURE, "User is blocked or inactive");
            throw new IllegalArgumentException("User is blocked or inactive");
        }

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                newUser.getPassword()
        ))
        {
            auditLogService.logSystemAction(AuditAction.LOGIN_FAILURE, "Bad credentials");
            throw new IllegalArgumentException("Invalid credentials");
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
