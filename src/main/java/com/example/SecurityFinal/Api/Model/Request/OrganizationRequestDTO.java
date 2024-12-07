package com.example.SecurityFinal.Api.Model.Request;


import com.example.SecurityFinal.Api.Model.BuisnessType;
import com.example.SecurityFinal.Api.Model.OrganizationType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationRequestDTO {
    private String icon;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 8, max = 16, message = "Name must be between 8 and 16 characters")
    private String name;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(min = 8, max = 16, message = "Phone number must be between 8 and 16 characters")
    private String phoneNumber;

    @NotBlank(message = "TRN cannot be blank")
    @Size(min = 8, max = 16, message = "TRN must be between 8 and 16 characters")
    private String trn;

    @NotNull(message = "Business type cannot be null")
    private BuisnessType businessType;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "email wrong format")
    private String email;

    @NotNull(message = "Organization type cannot be null")
    private OrganizationType organizationType;

    @NotBlank(message = "license cannot be blank")
    @Size(min = 8, max = 16, message = "license must be between 8 and 16 characters")
    private String license;

    @NotNull(message = "Expiry date cannot be blank")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    @NotNull(message = "Establishment date cannot be blank")
    @PastOrPresent(message = "Establishment date must be in the past")
    private LocalDate establishmentDate;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 8, max = 16, message = "Description must be between 8 and 16 characters")
    private String description;
}
