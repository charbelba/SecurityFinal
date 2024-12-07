package com.example.SecurityFinal.Api.Entity;

import com.example.SecurityFinal.Api.Model.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "org_project")
@Data
@NoArgsConstructor
public class Project extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;


    @Column(name = "uuid", updatable = false, nullable = false, unique = true)
    private String uuid;


    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Type cannot be blank")
    @Size(min = 3, max = 50, message = "Type must be between 3 and 50 characters")
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull(message = "Project status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "project_status", nullable = false)
    private ProjectStatus projectStatus;

    @NotBlank(message = "PRM Reference No cannot be blank")
    @Size(max = 50, message = "PRM Reference No must be less than 50 characters")
    @Column(name = "prm_reference_no", nullable = false)
    private String prmReferenceNo;

    @Size(max = 50, message = "Client Reference No must be less than 50 characters")
    @Column(name = "client_reference_no")
    private String clientReferenceNo;

    @NotBlank(message = "Client name cannot be blank")
    @Size(min = 3, max = 50, message = "Client name must be between 3 and 50 characters")
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @NotBlank(message = "Client country cannot be blank")
    @Size(min = 2, max = 50, message = "Client country must be between 2 and 50 characters")
    @Column(name = "client_country", nullable = false)
    private String clientCountry;

    @NotBlank(message = "Client address cannot be blank")
    @Size(min = 5, max = 100, message = "Client address must be between 5 and 100 characters")
    @Column(name = "client_address", nullable = false)
    private String clientAddress;

    @NotBlank(message = "Client phone number cannot be blank")
    @Size(min = 8, max = 20, message = "Client phone number must be between 8 and 20 characters")
    @Column(name = "client_phone_number", nullable = false)
    private String clientPhoneNumber;

    @NotBlank(message = "Client email cannot be blank")
    @Email(message = "Client email should be valid")
    @Column(name = "client_email", nullable = false)
    private String clientEmail;

    @Column(name = "attachment")
    private String attachment;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 255, message = "Description must be between 10 and 255 characters")
    @Column(name = "description", nullable = false)
    private String description;

    private int progress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    @JsonIgnore
    private Organization organization;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_userinfo",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<UserInfo> users = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private Set<Product> leads = new HashSet<>();


    @PrePersist
    protected void onCreate() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        progress = 0;
    }
}
