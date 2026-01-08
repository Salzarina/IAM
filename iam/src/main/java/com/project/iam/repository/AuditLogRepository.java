package com.project.iam.repository;

import com.project.iam.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Optional<AuditLog> findById(Long Id);

    /* Relation with User */
    List<AuditLog> findAllByUserId(Long UserId);
    List<AuditLog> findAllByUserIdAndAction(Long UserId, String Action);

    /* Relation with Time */
    List<AuditLog> findAllByTimestampAfter(LocalDateTime timestamp);
    List<AuditLog> findAllByTimestampBefore(LocalDateTime timestamp);
    List<AuditLog> findAllByTimestampBetween(LocalDateTime timestamp1, LocalDateTime timestamp2);
    Long deleteAllByTimestampBefore(LocalDateTime timestamp);

    /* Relation with Action */
    List<AuditLog> findAllByAction(String Action);
    Long countAllByAction(String Action);

}
