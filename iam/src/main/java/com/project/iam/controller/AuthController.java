package com.project.iam.controller;

import com.project.iam.model.AuthResponse;
import com.project.iam.model.LoginRequest;
import com.project.iam.model.RefreshToken;
import com.project.iam.model.User;
import com.project.iam.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    public AuthResponse login(LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    public void logout(RefreshToken refreshToken) {
        authService.logout(refreshToken);
    }

    public AuthResponse refreshToken(RefreshToken refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    public AuthResponse register(User user) {
        return authService.register(user);
    }

}
