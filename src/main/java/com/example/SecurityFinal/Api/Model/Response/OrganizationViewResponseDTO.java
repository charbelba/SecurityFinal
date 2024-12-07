package com.example.SecurityFinal.Api.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrganizationViewResponseDTO {
    private String uuid;
    private String name;
    private Long member_count;
    private Long projects_count;
    private String owner;
}
