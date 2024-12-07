package com.example.SecurityFinal.Api.Model.Response;

import lombok.Data;
import java.util.UUID;

@Data
public class ProjectResponseDTO {

    private String uuid;
    private String name;
    private String type;
    private String projectStatus;
    private String prmReferenceNo;
    private String clientReferenceNo;
    private String clientName;
    private String clientCountry;
    private String clientAddress;
    private String clientPhoneNumber;
    private String clientEmail;
    private String attachment;
    private String description;
}
