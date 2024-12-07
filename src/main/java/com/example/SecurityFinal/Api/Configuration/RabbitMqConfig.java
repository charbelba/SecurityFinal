package com.example.SecurityFinal.Api.Configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue inviteQueue() {
        return new Queue("inviteQueue", false); // Non-durable queue
    }

    @Bean
    public Queue verificationQueue() {
        return new Queue("verificationQueue", false); // Non-durable queue
    }
}
