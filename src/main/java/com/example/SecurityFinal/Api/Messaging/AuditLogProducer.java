package com.example.SecurityFinal.Api.Messaging;


import org.springframework.stereotype.Service;

@Service
public class AuditLogProducer {

 /*   private final RabbitTemplate rabbitTemplate;

    @Value("${audit.log.queue.name}")
    private String auditLogQueueName;

    public AuditLogProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAuditLog(AuditLog auditLog) {
        rabbitTemplate.convertAndSend(auditLogQueueName, auditLog);
    }*/
}
