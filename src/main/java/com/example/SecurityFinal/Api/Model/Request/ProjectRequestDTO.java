package com.example.SecurityFinal.Api.Model.Request;

import com.example.SecurityFinal.Api.Model.ProjectStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Type cannot be blank")
    @Size(min = 3, max = 50, message = "Type must be between 3 and 50 characters")
    private String type;

    @NotNull(message = "Project status cannot be null")
    private ProjectStatus projectStatus;

    @NotBlank(message = "PRM Reference No cannot be blank")
    @Size(max = 50, message = "PRM Reference No must be less than 50 characters")
    private String prmReferenceNo;

    @Size(max = 50, message = "Client Reference No must be less than 50 characters")
    private String clientReferenceNo;

    @NotBlank(message = "Client name cannot be blank")
    @Size(min = 3, max = 50, message = "Client name must be between 3 and 50 characters")
    private String clientName;

    @NotBlank(message = "Client country cannot be blank")
    @Size(min = 2, max = 50, message = "Client country must be between 2 and 50 characters")
    private String clientCountry;

    @NotBlank(message = "Client address cannot be blank")
    @Size(min = 5, max = 100, message = "Client address must be between 5 and 100 characters")
    private String clientAddress;

    @NotBlank(message = "Client phone number cannot be blank")
    @Size(min = 8, max = 20, message = "Client phone number must be between 8 and 20 characters")
    private String clientPhoneNumber;

    @NotBlank(message = "Client email cannot be blank")
    @Email(message = "Client email should be valid")
    private String clientEmail;

    private String attachment;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 255, message = "Description must be between 10 and 255 characters")
    private String description;


    private Set<Long> userIds;
}
