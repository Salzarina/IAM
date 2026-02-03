package com.project.iam.controller;


import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.AuditLog;
import com.project.iam.model.User;
import com.project.iam.service.AuditLogService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class AuditLogController {

    private AuditLogService auditLogService;

    public List<AuditLog> getAllAuditLogs(){
        return auditLogService.getAllAuditLogs();
    }

    public List<AuditLog> getAllAuditLogsByUser(User user) {
        return auditLogService.getAllAuditLogsByUser(user);
    }

    public List<AuditLog> getAllAuditLogsByUserAndAction(User user, AuditAction action) {
        return auditLogService.getAllAuditLogsByUserAndAction(user, action);
    }

    public List<AuditLog> getAllAuditLogsByUserName(String userName){
        return auditLogService.getAllAuditLogsByUserName(userName);
    }

    public List<AuditLog> getAllAuditLogsByAction(AuditAction action) {
        return auditLogService.getAllAuditLogsByAction(action);
    }

    public List<AuditLog> getAllAuditLogsByTimeBefore(LocalDateTime timeBefore) {
        return auditLogService.getAllAuditLogsByTimeBefore(timeBefore);
    }

    public List<AuditLog> getAllAuditLogsByTimeAfter(LocalDateTime timeAfter) {
        return auditLogService.getAllAuditLogsByTimeAfter(timeAfter);
    }

    public List<AuditLog> getAllAuditLogsBetweenTimestamp(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        return auditLogService.getAllAuditLogsBetweenTimestamp(startTimestamp, endTimestamp);
    }

    public Long getCountAllAuditLogsByAction(AuditAction action) {
        return auditLogService.getCountAllAuditLogsByAction(action);
    }

}
