package com.example.SecurityFinal.Api.Model.Response;

import com.example.SecurityFinal.Api.Model.BuisnessType;
import com.example.SecurityFinal.Api.Model.OrganizationType;
import lombok.Data;

@Data
public class OrganizationResponseDTO {
    private String uuid;
    private String icon;
    private String name;
    private String phoneNumber;
    private String trn;
    private BuisnessType businessType;
    private String email;
    private OrganizationType organizationType;
    private String license;
    private String expiryDate;
    private String establishmentDate;
    private String description;
}
