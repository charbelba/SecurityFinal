package com.example.SecurityFinal.Api.Controller;

import com.example.SecurityFinal.Api.Model.Request.InvitationRequestDTO;
import com.example.SecurityFinal.Api.Model.Request.OrganizationRequestDTO;
import com.example.SecurityFinal.Api.Model.Response.InvitationResponseDTO;
import com.example.SecurityFinal.Api.Model.Response.OrganizationResponseDTO;
import com.example.SecurityFinal.Api.Service.OrganizationService;
import com.example.SecurityFinal.Api.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organization")
@Validated
@Tag(name = "Organization Controller", description = "provides basic functionalities for organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;



    @Operation(summary = "retrieve organization",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(description = "retrieved organization", responseCode = "200"),
            })
    @GetMapping("/{organizationUUID}")
    public ResponseEntity<OrganizationResponseDTO> getOrganization(@PathVariable String organizationUUID){
        return ResponseEntity.ok(organizationService.getOrganization(organizationUUID));
    }

    @Operation(summary = "create organization",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "Organization Created", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrganizationRequestDTO.class))),
    })
    @PostMapping("/create")
    public ResponseEntity<OrganizationResponseDTO> create(@RequestBody @Valid OrganizationRequestDTO organizationDTO){
        return ResponseEntity.ok(organizationService.create(organizationDTO));
    }


    @Operation(summary = "delete organization",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "Organization Deleted", responseCode = "200"),
    })
    @DeleteMapping("/{organizationUUID}")
    //@PreAuthorize("@userService.hasOrganizationRole(#organizationUUID, 'ROLE_OWNER')")
    public ResponseEntity<Void> delete(@PathVariable String organizationUUID){
        organizationService.delete(organizationUUID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Invite to organization",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(description = "User invited", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InvitationRequestDTO.class))),
            })
    @PostMapping("/invite")
    //@PreAuthorize("@userService.hasOrganizationRole(#invitationRequestDTO.organizationUUID, 'ROLE_OWNER')")
    public ResponseEntity<InvitationResponseDTO> invite(@RequestBody @Valid InvitationRequestDTO invitationRequestDTO) {
        return ResponseEntity.ok(organizationService.invite(invitationRequestDTO));
    }



    @Operation(summary = "Accept Invitation",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "User invited", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = InvitationRequestDTO.class))),
    })
    @PostMapping("/acceptInvitation/{invitationUUID}")
    public ResponseEntity<Void> acceptInvitation(@PathVariable  @NotBlank String invitationUUID){
        organizationService.acceptInvitation(invitationUUID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "remove user",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(description = "User removed", responseCode = "200"),
            })
    @DeleteMapping("/{organizationUUID}/remove/{email}")
    public ResponseEntity<Void> removeUser(@PathVariable @NotBlank String organizationUUID,
                                           @PathVariable @NotBlank String email){
        organizationService.removeUser(organizationUUID, email);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "retrieve Invitations",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "retrieved invitations", responseCode = "200"),
    })
    @GetMapping("/invitations")
    public ResponseEntity<List<InvitationResponseDTO>> getAllInvitations(){
        return ResponseEntity.ok(organizationService.getAllInvitations());
    }



    @Operation(summary = "Assign role",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER),
                    @Parameter(name = "user", description = "user", required = true),
                    @Parameter(name = "roles", description = "roles", required = true)
            },
            responses = {
                    @ApiResponse(description = "Role(s) assigned", responseCode = "200"),
            })
    @GetMapping("/assign-role/{organizationUUID}")
    public ResponseEntity<Void> assignRole(@PathVariable String organizationUUID,
                                           @RequestParam("user") String user,
                                           @RequestParam("roles") List<String> roles) {
        organizationService.assignRole(organizationUUID, user, roles);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Revoke role",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER),

                    @Parameter(name = "user", description = "user", required = true),
                    @Parameter(name = "roles", description = "roles", required = true)


            },
            responses = {
                    @ApiResponse(description = "Role(s) revoked", responseCode = "200"),
            })
    @GetMapping("/revoke-role/{organizationUUID}")
    public ResponseEntity<Void> revokeRole(@PathVariable String organizationUUID,
                                           @RequestParam("user") String user,
                                           @RequestParam("roles") List<String> roles) {
        organizationService.revokeRole(organizationUUID, user, roles);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "get roles",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER),
                    @Parameter(name = "user", description = "user", required = true)

            },
            responses = {
                    @ApiResponse(description = "Role(s) retrieved", responseCode = "200"),
            })
    @GetMapping("/roles/{organizationUUID}")
    public ResponseEntity<List<String>> getRoles(@PathVariable String organizationUUID,
                                           @RequestParam("user") String user) {
        return ResponseEntity.ok(organizationService.getRoles(organizationUUID, user));
    }


    @Operation(summary = "patch organization",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER),
                    @Parameter(name = "organizationUUID", description = "organizationUUID", required = true),

            },
            responses = {
                    @ApiResponse(description = "organization patched", responseCode = "200"),
            })
    @PatchMapping("/{organizationUUID}")
    public ResponseEntity<Void> patchOrganization(@PathVariable String organizationUUID,
                                              @RequestBody Map<String,Object> values) throws MethodArgumentNotValidException {
        organizationService.patchOrganization(organizationUUID, values);
        return ResponseEntity.ok().build();
    }



}
