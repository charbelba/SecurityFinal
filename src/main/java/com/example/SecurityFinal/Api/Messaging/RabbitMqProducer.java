package com.example.SecurityFinal.Api.Messaging;

import com.example.SecurityFinal.Api.Exception.ApiRequestException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RabbitMqProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @CircuitBreaker(name = "rabbitmqService", fallbackMethod = "rabbitMQFallback")
    public void sendInvite(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }


    @CircuitBreaker(name = "rabbitmqService", fallbackMethod = "rabbitMQFallback")
    public void sendVerification(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

    public void rabbitMQFallback(String message, String arg, Throwable t) {
        log.info("RabbitMQ is down, fallback initiated: " + message);
        throw new ApiRequestException("rabbit mq is down retry later");
    }




}
