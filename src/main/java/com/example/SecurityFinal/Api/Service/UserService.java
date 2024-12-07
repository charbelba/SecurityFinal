package com.example.SecurityFinal.Api.Service;

import com.example.SecurityFinal.Api.Entity.*;
import com.example.SecurityFinal.Api.Exception.*;
import com.example.SecurityFinal.Api.Messaging.RabbitMqProducer;
import com.example.SecurityFinal.Api.Model.Request.LoginRequestDTO;
import com.example.SecurityFinal.Api.Model.Request.SignUpRequestDTO;
import com.example.SecurityFinal.Api.Model.Response.JwtResponseDTO;
import com.example.SecurityFinal.Api.Model.Response.OrganizationViewResponseDTO;
import com.example.SecurityFinal.Api.Repository.*;
import com.example.SecurityFinal.Api.Security.SecurityContext;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final SecurityContext securityContext;
    private final ProjectRepository projectRepository;
    private final RabbitMqProducer rabbitMqProducer;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;




    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService,
                       RoleRepository roleRepository, OrganizationRepository organizationRepository,
                       SecurityContext securityContext, ProjectRepository projectRepository,
                       RabbitMqProducer rabbitMqProducer, VerificationTokenRepository verificationTokenRepository,
                       MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.organizationRepository = organizationRepository;
        this.securityContext = securityContext;
        this.projectRepository = projectRepository;
        this.rabbitMqProducer = rabbitMqProducer;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailService = mailService;
    }



    public JwtResponseDTO signin(@Valid LoginRequestDTO loginRequestDTO) {
        if(!userRepository.existsByEmail(loginRequestDTO.getEmail())) throw new UserNotFoundException("User not found");
        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                log.info("User authenticated successfully");

                Optional<UserInfo> user = userRepository.findByEmail(loginRequestDTO.getEmail());
                if(!user.get().isVerified()) throw new NotVerifiedException("user not verified");
                return JwtResponseDTO.builder()
                        .accessToken(jwtService.GenerateToken(loginRequestDTO.getEmail()))
                        .email(user.get().getEmail())
                        .username(user.get().getUsername())
                        .pfp(user.get().getPfp()).build();
            } else {
                log.warn("Authentication failed");
                throw new UsernameNotFoundException("email not found!");
            }
        } catch (Exception e) {
            log.error("Authentication process failed", e);
            throw e;
        }
    }


    //@Transactional
    public void signup(SignUpRequestDTO signUpRequestDTO, MultipartFile profilePic)  {
        if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) throw new UserAlreadyExistException("User already exists with this email!");

        Set<UserRole> roles = new HashSet<>();
            roles.add(roleRepository.findById(1L).orElseThrow(() -> new ApiRequestException("role not found")));
            String encodedPassword = passwordEncoder.encode(signUpRequestDTO.getPassword());

        signUpRequestDTO.setPassword(encodedPassword);

            UserInfo user = userRepository.save(UserInfo.builder().email(signUpRequestDTO.getEmail())
                    .password(encodedPassword)
                    .username(signUpRequestDTO.getUsername())
                    .pfp("/dummy/url")
                    .roles(roles)
                    .organizationRoles(new HashSet<>())
                    .build());

        VerificationToken token = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        verificationTokenRepository.save(token);



        String message = "email=" + user.getEmail() + ";token=" + token.getToken();

        /*try {
            mailService.sendVerificationEmail(user.getEmail(),"Verification", token.getToken());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }*/

        rabbitMqProducer.sendVerification("verificationQueue", message);

        /*return JwtResponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(signUpRequestDTO.getEmail()))
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .pfp(user.getPfp()).build();*/
    }



    public boolean hasOrganizationRole(String uuid, String roleName) {


        UserInfo user = securityContext.getCurrentUser();

        Organization organization = organizationRepository.findByUuid(uuid)
                .orElseThrow(() -> new ApiRequestException("bad request"));


        return user.getOrganizationRoles().stream()
                .filter(role -> role.getOrganization().getId() == organization.getId())
                .anyMatch(role -> role.getName().equals(roleName));
    }


    public List<UserInfo> getOrganizationUsers(String organizationUUID) {
        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));
        return userRepository.findAllUsersByOrganizationId(organization.getId());
    }

    public List<UserInfo> getProjectUsers(String projectUUID) {
        Project project = projectRepository.findByUuid(projectUUID)
                .orElseThrow(() -> new EntityNotFoundException("project not found"));
        return userRepository.findAllUsersByProjectId(project.getId());
    }

    public List<OrganizationViewResponseDTO> getUserOrganizations() {
        return organizationRepository.getOrganizationViewsByUser(securityContext.getCurrentUser().getEmail());
    }


    public void hasRole(String organizationUUID, List<String> roleNames) {
        UserInfo user = securityContext.getCurrentUser();

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        boolean result = user.getOrganizationRoles().stream()
                .filter(role -> role.getOrganization().getId() == organization.getId())
                .anyMatch(role -> roleNames.contains(role.getName()));

        if (!result) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public void verify(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
           throw new ApiRequestException("bad request");
        }

        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) throw new VerificationTokenExpired("verification token expired");


        UserInfo user = verificationToken.getUser();
        if(user.isVerified()) throw new ApiRequestException("user already verified");
        user.setVerified(true);
        userRepository.save(user);


    }
}