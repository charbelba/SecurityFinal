package com.example.SecurityFinal.Api.Security;

import com.example.SecurityFinal.Api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
class SecurityAuditAware implements AuditorAware<String> {

    private final SecurityContext securityContext;
    private final UserRepository userRepository;

    @Autowired
    public SecurityAuditAware(SecurityContext securityContext, UserRepository userRepository) {
        this.securityContext = securityContext;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("1");  // Replace with dynamic auditor if needed
    }
}