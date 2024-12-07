    package com.example.SecurityFinal.Api.Entity;

    import com.example.SecurityFinal.Api.Model.BuisnessType;
    import com.example.SecurityFinal.Api.Model.OrganizationType;
    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;
    import java.util.*;

    @Entity
    @Table(name = "org_organization")
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public class Organization extends BaseEntity{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        @JsonIgnore
        private long id;

        @Column(name = "uuid", updatable = false, nullable = false, unique = true)
        private String uuid;

        @OneToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private UserInfo owner;

        private String icon;

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 8, max = 16, message = "Name must be between 8 and 16 characters")
        @Column(length = 16, nullable = false)
        private String name;

        @NotBlank(message = "Phone number cannot be blank")
        @Size(min = 8, max = 16, message = "Phone number must be between 8 and 16 characters")
        @Column(name = "phone_number", length = 16, nullable = false)
        private String phoneNumber;

        @NotBlank(message = "TRN cannot be blank")
        @Size(min = 8, max = 16, message = "TRN must be between 8 and 16 characters")
        @Column(length = 16, nullable = false)
        private String trn;

        @NotNull(message = "Business type cannot be null")
        @Enumerated(EnumType.STRING)
        @Column(name = "business_type", nullable = false)
        private BuisnessType businessType;

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        @Column(nullable = false)
        private String email;

        @NotNull(message = "Organization type cannot be null")
        @Enumerated(EnumType.STRING)
        @Column(name = "organization_type", nullable = false)
        private OrganizationType organizationType;

        @NotBlank(message = "license cannot be blank")
        @Size(min = 8, max = 16, message = "license must be between 8 and 16 characters")
        @Column(length = 16, nullable = false)
        private String license;

        @Future(message = "Expiry date must be in the future")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Column(name = "expiry_date", nullable = false)
        private LocalDate expiryDate;

        @Past(message = "Establishment date must be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Column(name = "establishment_date", nullable = false)
        private LocalDate establishmentDate;

        @NotBlank(message = "Description cannot be blank")
        @Size(min = 8, max = 255, message = "Description must be between 8 and 255 characters")
        @Column(nullable = false)
        private String description;


        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "user_organization",
                joinColumns = @JoinColumn(name = "organization_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        @JsonIgnore
        private List<UserInfo> users;

        @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<Project> projects = new ArrayList<>();



        @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<Product> purchaseOrders = new ArrayList<>();


        @OneToMany(mappedBy = "organization", cascade = {CascadeType.ALL, CascadeType.REMOVE}, orphanRemoval = true)
        @JsonIgnore
        private Set<OrganizationRole> roles = new HashSet<>();

        @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<Product> products = new ArrayList<>();


        @PrePersist
        protected void onCreate() {
            if (uuid == null) {
                uuid = UUID.randomUUID().toString();
            }
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Organization)) return false;
            Organization organization = (Organization) o;
            return id == organization.id;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(id);
        }
    }
