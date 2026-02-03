package com.project.iam.service;

import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.AuditLog;
import com.project.iam.model.User;
import com.project.iam.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logUserAction(User user, AuditAction name, String description) {
        AuditLog auditLog = AuditLog.builder()
                .user(user)
                .action(name)
                .timestamp(LocalDateTime.now())
                .description(description)
                .build();

        auditLogRepository.save(auditLog);
    }

    public void logSystemAction(AuditAction name, String description) {
        AuditLog auditLog = AuditLog.builder()
                .action(name)
                .timestamp(LocalDateTime.now())
                .description(description)
                .build();
        auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getAllAuditLogsByUser(User user) {
        return auditLogRepository.findAllByUserId(user.getId());
    }

    public List<AuditLog> getAllAuditLogsByUserAndAction(User user, AuditAction action) {
        return auditLogRepository.findAllByUserIdAndAction(user.getId(), action);
    }

    public List<AuditLog> getAllAuditLogsByUserName(String userName) {
        return auditLogRepository.findAllByUserName(userName);
    }

    public List<AuditLog> getAllAuditLogsByAction(AuditAction action) {
        return auditLogRepository.findAllByAction(action);
    }

    public List<AuditLog> getAllAuditLogsByTimeBefore(LocalDateTime timeBefore) {
        return auditLogRepository.findAllByTimestampBefore(timeBefore);
    }

    public List<AuditLog> getAllAuditLogsByTimeAfter(LocalDateTime timeAfter) {
        return auditLogRepository.findAllByTimestampAfter(timeAfter);
    }

    public List<AuditLog> getAllAuditLogsBetweenTimestamp(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        return auditLogRepository.findAllByTimestampBetween(startTimestamp, endTimestamp);
    }

    public Long getCountAllAuditLogsByAction(AuditAction action) {
        return auditLogRepository.countAllByAction(action);
    }

}
