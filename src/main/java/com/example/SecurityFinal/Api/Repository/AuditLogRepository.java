package com.example.SecurityFinal.Api.Repository;

import com.example.SecurityFinal.Api.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Custom query methods if needed
}
