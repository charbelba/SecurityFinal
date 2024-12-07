package com.example.SecurityFinal.Api.Security;

import com.example.SecurityFinal.Api.Entity.AuditLog;
import com.example.SecurityFinal.Api.Repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditListener {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuditListener(@Lazy AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());  // Register the JavaTimeModule
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Prevents writing dates as timestamps
    }

    @PrePersist
    public void prePersist(Object entity) {
        createAuditLog(entity, "CREATE", null);
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        createAuditLog(entity, "UPDATE", entity);
    }

    @PreRemove
    public void preRemove(Object entity) {
        createAuditLog(entity, "DELETE", entity);
    }

    private void createAuditLog(Object entity, String action, Object oldEntity) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setEntityName(entity.getClass().getSimpleName());
            auditLog.setEntityId(getEntityId(entity));
            auditLog.setAction(action);
            auditLog.setPerformedBy("1");
            auditLog.setPerformedAt(LocalDateTime.now());

            String newData = objectMapper.writeValueAsString(entity);  // This now handles LocalDate
            auditLog.setNewData(newData);

            if (oldEntity != null) {
                String oldData = objectMapper.writeValueAsString(oldEntity);
                auditLog.setOldData(oldData);
            }

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create audit log", e);
        }
    }

    private String getEntityId(Object entity) {
        try {
            return "uknown";
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get ID from entity", e);
        }
    }
}
