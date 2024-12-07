package com.example.SecurityFinal.Api.Controller;

import com.example.SecurityFinal.Api.Entity.UserInfo;
import com.example.SecurityFinal.Api.Exception.ApiRequestException;
import com.example.SecurityFinal.Api.Model.Request.LoginRequestDTO;
import com.example.SecurityFinal.Api.Model.Request.SignUpRequestDTO;
import com.example.SecurityFinal.Api.Model.Response.JwtResponseDTO;
import com.example.SecurityFinal.Api.Model.Response.OrganizationViewResponseDTO;
import com.example.SecurityFinal.Api.Security.SecurityContext;
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
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
@Tag(name = "User Controller", description = "provides basic functionalities for users")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private SecurityContext securityContext;
    @Operation(summary = "login user", responses = {
            @ApiResponse(description = "User found", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDTO.class))),
            @ApiResponse(description = "User not found", responseCode = "404")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(userService.signin(loginRequestDTO));

    }

    @Operation(summary = "Sign up user",
            responses = {

            @ApiResponse(description = "User authenticated successfully!", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignUpRequestDTO.class))),
            @ApiResponse(description = "User already exists", responseCode = "404")
    })
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
             @Valid SignUpRequestDTO signUpRequestDTO) {
        userService.signup(signUpRequestDTO, null);
        return ResponseEntity.ok( ).build();
    }


    @Operation(
            summary = "Verify user",
            parameters = {
                    @Parameter(name = "token", description = "Verification token for the user", required = true)
            },
            responses = {
                    @ApiResponse(description = "User authenticated successfully!", responseCode = "200")
            }
    )
    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam(name = "token") String token) {
        userService.verify(token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Return session",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(description = "Session retrieved successfully", responseCode = "200"),
                    @ApiResponse(description = "User not found", responseCode = "404")
            })
    @GetMapping("/session")
    public ResponseEntity<JwtResponseDTO> me(){
        UserInfo user = securityContext.getCurrentUser();
        return ResponseEntity.ok(JwtResponseDTO.builder().username(user.getUsername()).pfp(user.getPfp()).email(user.getEmail()).build());
    }


    @Operation(summary = "retrieve users",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "Users retrieved Successfully!", responseCode = "200")
    })
    @GetMapping("/organization/{organizationUUID}/users")
    public ResponseEntity<List<UserInfo>> getOrganizationUsers(@PathVariable @NotBlank String organizationUUID){
        return ResponseEntity.ok(userService.getOrganizationUsers(organizationUUID));
    }

    @Operation(summary = "retrieve project users",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
            @ApiResponse(description = "Users retrieved Successfully!", responseCode = "200")
    })
    @GetMapping("/project/{projectUUID}/users")
    public ResponseEntity<List<UserInfo>> getProjectUsers(@PathVariable @NotBlank String projectUUID){
        return ResponseEntity.ok(userService.getProjectUsers(projectUUID));
    }


    @Operation(summary = "retrieve organizations",
            parameters = {
                    @Parameter(name = "Authorization",
                            description = "Bearer token for authentication",
                            required = true,
                            in = ParameterIn.HEADER)
            },
            responses = {
                    @ApiResponse(description = "Organizations retrieved Successfully!!", responseCode = "200")
            })
    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationViewResponseDTO>> getUserOrganizations(){
        return ResponseEntity.ok(userService.getUserOrganizations());
    }


}
