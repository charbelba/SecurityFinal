package com.example.SecurityFinal.Api.Messaging;

import com.example.SecurityFinal.Api.Service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RabbitMqConsumer {

    @Autowired
    private MailService mailService;

    @RabbitListener(queues = "inviteQueue")
    public void receiveInvite(String message) {

        String[] parts = message.split(";");
        String email = parts[0].split("=")[1];
        String token = parts[1].split("=")[1];
        log.info("Sending Invite to : " + email);

        try {
            mailService.sendInviteEmail(email, "You're Invited!", token);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }
    }


    @RabbitListener(queues = "verificationQueue")
    public void receiveVerification(String message) {

        String[] parts = message.split(";");
        String email = parts[0].split("=")[1];
        String token = parts[1].split("=")[1];
        log.info("Sending Invite to : " + email);

        try {
            mailService.sendVerificationEmail(email,"Verification Email",token);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }
    }



}
