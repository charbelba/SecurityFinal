package com.example.SecurityFinal.Api.Messaging;

import org.springframework.stereotype.Service;

@Service
public class AuditLogConsumer {
/*
    private final AuditLogRepository auditLogRepository;

    public AuditLogConsumer(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @RabbitListener(queues = "${audit.log.queue.name}")
    public void consumeAuditLog(AuditLog auditLog) {
        try {
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to process audit log", e);
        }
    }*/
}
