package com.example.SecurityFinal.Api.Service;

import com.example.SecurityFinal.Api.Entity.Organization;
import com.example.SecurityFinal.Api.Entity.Project;
import com.example.SecurityFinal.Api.Entity.UserInfo;
import com.example.SecurityFinal.Api.Exception.UserNotFoundException;
import com.example.SecurityFinal.Api.Model.Request.ProjectRequestDTO;
import com.example.SecurityFinal.Api.Model.Response.ProjectResponseDTO;
import com.example.SecurityFinal.Api.Repository.OrganizationRepository;
import com.example.SecurityFinal.Api.Repository.ProjectRepository;
import com.example.SecurityFinal.Api.Repository.UserRepository;
import com.example.SecurityFinal.Api.Security.SecurityContext;
import com.example.SecurityFinal.Api.Util.PagedResponse;
import com.example.SecurityFinal.Api.Util.ValidatorUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityContext securityContext;


    @Autowired
    private ModelMapper modelMapper;
    public ProjectResponseDTO create(String organizationUUID, ProjectRequestDTO projectRequestDTO) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));

        Project project = modelMapper.map(projectRequestDTO, Project.class);

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow( () -> new EntityNotFoundException("organization not found"));

        project.setOrganization(organization);

        Project savedProject = projectRepository.save(project);

        return modelMapper.map(savedProject, ProjectResponseDTO.class);
    }

    public PagedResponse<Project> getProjects(String organizationUUID, Pageable pageable) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow( () -> new EntityNotFoundException("organization not found"));
        return PagedResponse.fromPage(projectRepository.findByOrganizationId(organization.getId(), pageable));
    }

    public Boolean addMember(String organizationUUID, String projectUUID, String email) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER","ROLE_OWNER"));

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow( () -> new EntityNotFoundException("organization not found"));

        Project project = projectRepository.findByUuidAndOrganizationId(projectUUID, organization.getId())
                .orElseThrow(() -> new EntityNotFoundException("project not found"));

        UserInfo invitee = userRepository.findByEmailAndOrganizationId(email, organization.getId())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        project.getUsers().add(invitee);
        projectRepository.save(project);

        return true;

    }


    public void delete(String organizationUUID, String projectUUID) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow( () -> new EntityNotFoundException("organization not found"));

        Project project = projectRepository.findByUuidAndOrganizationId(projectUUID, organization.getId())
                .orElseThrow(() -> new EntityNotFoundException("project not found"));

        projectRepository.delete(project);
    }

    public Project getProject(String organizationUUID, String projectUUID) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER"));

        return projectRepository.findByUuid(projectUUID)
                .orElseThrow(() -> new EntityNotFoundException("project not found"));
    }


    public Boolean removeMember(String organizationUUID, String projectUUID, String email) {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER","ROLE_OWNER"));

        Organization organization = organizationRepository.findByUuid(organizationUUID)
                .orElseThrow( () -> new EntityNotFoundException("organization not found"));

        Project project = projectRepository.findByUuidAndOrganizationId(projectUUID, organization.getId())
                .orElseThrow(() -> new EntityNotFoundException("project not found"));

        UserInfo invitee = userRepository.findByEmailAndProjectId(email, project.getId())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        project.getUsers().remove(invitee);
        projectRepository.save(project);

        return true;
    }

    public void patchProject(String organizationUUID, String projectUUID, Map<String, Object> values) throws MethodArgumentNotValidException {
        userService.hasRole(organizationUUID, List.of("ROLE_MEMBER","ROLE_OWNER"));

        Optional<Project> optionalProject = projectRepository.findByOrganization_UuidAndUuid(organizationUUID, projectUUID);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();

            ValidatorUtils.validateAndApplyFields(project, new ProjectRequestDTO(), values, Project.class, ProjectRequestDTO.class);


            projectRepository.save(project);
        } else {
            throw new EntityNotFoundException("project not found ");
        }
    }
}
