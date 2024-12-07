package com.example.SecurityFinal.Api;

import com.example.SecurityFinal.Api.Entity.UserRole;
import com.example.SecurityFinal.Api.Repository.RoleRepository;
import com.example.SecurityFinal.Api.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

@SpringBootApplication

@EnableJpaAuditing
public class SecurityFinalApplication {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender mailSender;


	public static void main(String[] args) {
		SpringApplication.run(SecurityFinalApplication.class, args);
	}
	@PostConstruct
	public void init() {
		UserRole role = UserRole.builder().name("ROLE_USER").build();
		UserRole developer = UserRole.builder().name("ROLE_DEVELOPER").build();
		roleRepository.saveAll(List.of(role, developer));
		System.out.println(mailSender);
	}



}
