package com.project.iam.repository;

import com.project.iam.model.RefreshToken;
import com.project.iam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByValue(String value);

    Optional<RefreshToken> findByValue(String value);
    Optional<RefreshToken> findByUserId(Long userId);
    Optional<RefreshToken> findByUserAndRevokedFalse(User user);

    long deleteByUserId(Long userId);
    long deleteAllByExpiryDateBefore(LocalDateTime expiryDate);
}
