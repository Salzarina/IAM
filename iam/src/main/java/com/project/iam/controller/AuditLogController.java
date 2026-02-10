package com.project.iam.controller;


import com.project.iam.enumerations.AuditAction;
import com.project.iam.model.AuditLog;
import com.project.iam.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auditlogs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public List<AuditLog> getAllAuditLogs() {
        return auditLogService.getAllAuditLogs();
    }

    @GetMapping("/user/{username}")
    public List<AuditLog> getByUsername(@PathVariable String username) {
        return auditLogService.getAllAuditLogsByUserName(username);
    }

    @GetMapping("/action/{action}")
    public List<AuditLog> getByAction(@PathVariable AuditAction action) {
        return auditLogService.getAllAuditLogsByAction(action);
    }

    @GetMapping("/after/{timeAfter}")
    public List<AuditLog> getAfter(@PathVariable LocalDateTime timeAfter) {
        return auditLogService.getAllAuditLogsByTimeAfter(timeAfter);
    }

    @GetMapping("/before/{timeBefore}")
    public List<AuditLog> getBefore(@PathVariable LocalDateTime timeBefore) {
        return auditLogService.getAllAuditLogsByTimeBefore(timeBefore);
    }

    @GetMapping("/between")
    public List<AuditLog> getBetween(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        return auditLogService.getAllAuditLogsBetweenTimestamp(from, to);
    }

    @GetMapping("/count/{action}")
    public Long countByAction(@PathVariable AuditAction action) {
        return auditLogService.getCountAllAuditLogsByAction(action);
    }
}
