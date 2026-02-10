package com.project.iam.controller;

import com.project.iam.model.*;
import com.project.iam.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody RefreshToken refreshToken) {
        authService.logout(refreshToken);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshToken refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegRequest regRequest) {
        return authService.register(regRequest);
    }

}
