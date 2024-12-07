package com.example.SecurityFinal.Api.Service;

import com.example.SecurityFinal.Api.Entity.Invitation;
import com.example.SecurityFinal.Api.Entity.Organization;
import com.example.SecurityFinal.Api.Entity.OrganizationRole;
import com.example.SecurityFinal.Api.Entity.UserInfo;
import com.example.SecurityFinal.Api.Exception.*;
import com.example.SecurityFinal.Api.Messaging.RabbitMqProducer;
import com.example.SecurityFinal.Api.Model.Request.InvitationRequestDTO;
import com.example.SecurityFinal.Api.Model.Request.OrganizationRequestDTO;
import com.example.SecurityFinal.Api.Model.Response.InvitationResponseDTO;
import com.example.SecurityFinal.Api.Model.Response.OrganizationResponseDTO;
import com.example.SecurityFinal.Api.Repository.InvitationRepository;
import com.example.SecurityFinal.Api.Repository.OrganizationRepository;
import com.example.SecurityFinal.Api.Repository.OrganizationRoleRepository;
import com.example.SecurityFinal.Api.Repository.UserRepository;
import com.example.SecurityFinal.Api.Security.SecurityContext;
import com.example.SecurityFinal.Api.Util.ValidatorUtils;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private InvitationRepository invitationRepository;


    @Autowired
    private OrganizationRoleRepository organizationRoleRepository;


    @Autowired
    private RabbitMqProducer rabbitMqProducer;

    @Autowired
    private UserService userService;


    @Autowired
    private OrganizationRoleRepository roleRepository;

    @Autowired
    private SecurityContext securityContext;


    @Transactional
    public OrganizationResponseDTO create(OrganizationRequestDTO organizationRequestDTO) {
        UserInfo user = securityContext.getCurrentUser();

        if (organizationRepository.existsByOwner(user)) {
            throw new OrganizationAlreadyExistException("user already has an organization");
        }

        Organization organization = modelMapper.map(organizationRequestDTO, Organization.class);
        organization.setOwner(user);
        organization.setUsers(new ArrayList<>(Collections.singleton(user)));
        organization.setRoles(new HashSet<>());

        OrganizationRole role = OrganizationRole.builder().name("ROLE_OWNER").organization(organization).build();
        OrganizationRole roleMember = OrganizationRole.builder().name("ROLE_MEMBER").organization(organization).build();
        organization.getRoles().addAll(List.of(role, roleMember));


        Organization savedOrganization = organizationRepository.save(organization);


        user.getOrganizationRoles().addAll(List.of(role, roleMember));

        return modelMapper.map(savedOrganization, OrganizationResponseDTO.class);
    }


    @Autowired
    private MailService mailService;

    //@Transactional(rollbackOn = Exception.class)
    public InvitationResponseDTO invite(InvitationRequestDTO invitationRequestDTO){

        Organization organization = organizationRepository.findByUuid(invitationRequestDTO.getOrganizationUUID())
                .orElseThrow(() ->  new EntityNotFoundException("organization not found"));
        userService.hasRole(organization.getUuid(), List.of("ROLE_MEMBER"));


        if(invitationRepository.existsByInviteeEmail(invitationRequestDTO.getInviteeEmail())) throw new InvitationAlreadyExistException("This user is already invited");
        Optional<UserInfo> u = userRepository.findByEmailAndOrganizationId(invitationRequestDTO.getInviteeEmail(), organization.getId());

        if(u.isPresent())throw new UserAlreadyInvitedException("user already invited");

        Invitation invitation = modelMapper.map(invitationRequestDTO, Invitation.class);

        UserInfo invitee = userRepository.findByEmail(invitationRequestDTO.getInviteeEmail())
                .orElseThrow(() ->  new UserNotFoundException("user not found exception"));

        invitation.setInvitee(invitee);
        invitation.setUuid(UUID.randomUUID().toString());


        Invitation savedInvitation = invitationRepository.save(invitation);

        try {
            mailService.sendInviteEmail(invitationRequestDTO.getInviteeEmail(),"you are invited ", savedInvitation.getUuid());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        String message = "email=" + invitee.getEmail() + ";token=" + organization.getUuid();

        rabbitMqProducer.sendInvite("inviteQueue", message);

        return modelMapper.map(savedInvitation, InvitationResponseDTO.class);

    }

    @Transactional
    public void acceptInvitation(String invitationUUID) {
        UserInfo user = securityContext.getCurrentUser();

        Invitation invitation = invitationRepository.findByUuid(invitationUUID)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        if(!invitation.getInvitee().getEmail().equals(user.getEmail())) throw new ApiRequestException("not invited");

        Organization organization = organizationRepository.findByUuid(invitation.getOrganizationUUID())
                        .orElseThrow(()->new EntityNotFoundException("organization not found"));

        organization.getUsers().add(invitation.getInvitee());

        user.getOrganizationRoles().addAll(roleRepository.findByNameIn(List.of("ROLE_MEMBER")));


        organizationRepository.save(organization);

        invitationRepository.delete(invitation);

    }


    public void delete(String organizationUUID) {
        userService.hasRole(organizationUUID, List.of("ROLE_OWNER"));

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                        .orElseThrow(() -> new EntityNotFoundException("Organization not found"));

        organizationRepository.delete(organization);


    }


    public List<InvitationResponseDTO> getAllInvitations() {
        UserInfo user = securityContext.getCurrentUser();
        return invitationRepository.findByInvitee(user)
                .stream()
                .map(invitation -> modelMapper.map(invitation, InvitationResponseDTO.class))
                .collect(Collectors.toList());
    }


    public OrganizationResponseDTO getOrganization(String organizationUUID) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));

        return modelMapper.map(organizationRepository.findByUuid(organizationUUID)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found")), OrganizationResponseDTO.class);
    }

    public void revokeRole(String organizationUUID, String email, List<String> roles) {
        userService.hasRole(organizationUUID, List.of("ROLE_OWNER"));


        UserInfo userInfo = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<OrganizationRole> rolesToRevoke = roleRepository.findByNameIn(roles);

        if (rolesToRevoke.isEmpty()) {
            throw new EntityNotFoundException("Roles not found");
        }

        rolesToRevoke.forEach(role -> {
            if (userInfo.getOrganizationRoles().contains(role)) {
                userInfo.getOrganizationRoles().remove(role);
            }
        });

        userRepository.save(userInfo);
    }


    public void assignRole(String organizationUUID, String email, List<String> roles) {
        userService.hasRole(organizationUUID, List.of("ROLE_OWNER"));

        UserInfo userInfo = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<OrganizationRole> rolesToAssign = roleRepository.findByNameIn(roles);

        if (rolesToAssign.isEmpty()) {
            throw new EntityNotFoundException("Roles not found");
        }

        rolesToAssign.forEach(role -> {
            if (!userInfo.getRoles().contains(role)) {
                userInfo.getOrganizationRoles().add(role);
            }
        });

        userRepository.save(userInfo);
    }


    public List<String> getRoles(String organizationUUID, String email) {
        if(!organizationRepository.existsByUuid(organizationUUID)) throw new EntityNotFoundException("organization not found");

         userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return roleRepository.findRolesByUserAndOrganization(email, organizationUUID);
    }


    public void patchOrganization(String organizationUUID, Map<String, Object> values) throws MethodArgumentNotValidException {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));

        Optional<Organization> optionalOrganization = organizationRepository.findByUuid(organizationUUID);

        if (optionalOrganization.isPresent()) {
            Organization organization = optionalOrganization.get();


            ValidatorUtils.validateAndApplyFields(organization, new OrganizationRequestDTO(), values, Organization.class, OrganizationRequestDTO.class);


            organizationRepository.save(organization);
        } else {
            throw new EntityNotFoundException("Organization not found ");
        }
    }

    @Transactional
    public void removeUser(String organizationUUID, String email) {
        userService.hasRole(organizationUUID, List.of("ROLE_OWNER"));

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow(()->new EntityNotFoundException("organization not found"));

        UserInfo user = organization.getUsers().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));

        user.getOrganizationRoles().clear();

        userRepository.save(user);

        organization.getUsers().remove(user);

        organizationRepository.save(organization);

    }
}
