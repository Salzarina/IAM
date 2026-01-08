package com.project.iam.repository;

import com.project.iam.model.Token;
import com.project.iam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    boolean existsByValue(String value);

    Optional<Token> findByValue(String value);
    Optional<Token> findByUserId(Long userId);

    long deleteByUserId(Long userId);
    long deleteAllByExpiryDateBefore(LocalDateTime expiryDate);
}
