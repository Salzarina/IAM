package com.project.iam.service;

import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.RefreshToken;
import com.project.iam.model.Role;
import com.project.iam.model.User;
import com.project.iam.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final AuditLogService auditLogService;

    public TokenService(TokenRepository tokenRepository, AuditLogService auditLogService) {
        this.tokenRepository = tokenRepository;
        this.auditLogService = auditLogService;
    }

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 error", e);
        }
    }
    public String generateRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(sha256(UUID.randomUUID().toString()))
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        tokenRepository.save(refreshToken);

        auditLogService.logUserAction(
                user,
                AuditAction.TOKEN_CREATE,
                "Refresh token created"
        );

        return refreshToken.getTokenHash();
    }


    public RefreshToken refreshToken(User user) {
        tokenRepository.findByUserAndRevokedFalse(user)
                .ifPresent(oldToken -> {
                    oldToken.setRevoked(true);
                    tokenRepository.save(oldToken);
                });

        RefreshToken newToken = RefreshToken.builder()
                .user(user)
                .tokenHash(sha256(UUID.randomUUID().toString()))
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();

        tokenRepository.save(newToken);

        auditLogService.logUserAction(user, AuditAction.TOKEN_REFRESH,
                "Refresh token created for user " + user.getUsername());

        return newToken;
    }

    public RefreshToken revokeToken(User user) {
        RefreshToken token = tokenRepository.findByUserAndRevokedFalse(user)
                .orElseThrow(() -> new RuntimeException("No active refresh token found"));

        token.setRevoked(true);
        tokenRepository.save(token);

        auditLogService.logUserAction(user, AuditAction.TOKEN_REVOKED,
                "Refresh token revoked for user " + user.getUsername());

        return token;
    }

    public boolean validateRefreshToken(RefreshToken token) {
        return token != null
                && !token.isRevoked()
                && token.getExpiresAt().isAfter(LocalDateTime.now());
    }

    public User getUserByToken(RefreshToken refreshToken) {
        return refreshToken.getUser();
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        long ACCESS_TOKEN_TTL = 15 * 60 * 1000;

        String SECRET_KEY = "supersecretkeysupersecretkey1234";

        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_TTL);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles()
                        .stream()
                        .map(Role::getRoleName)
                        .toList())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
