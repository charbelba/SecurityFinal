package com.example.SecurityFinal.Api.Model.Request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvitationRequestDTO {

    @NotBlank(message = "organizationUUID cannot be blank")
    private String organizationUUID;

    @NotBlank(message = "inviteeEmail cannot be null")
    @Email
    private String inviteeEmail;
}
