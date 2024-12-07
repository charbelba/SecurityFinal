package com.example.SecurityFinal.Api.Entity;

import com.example.SecurityFinal.Api.Model.ProductType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false, unique = true)
    private String uuid;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Icon is required")
    private String icon;

    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String code;

    @NotNull(message = "Product type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @FutureOrPresent(message = "Expiry date must be in the present or future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Positive(message = "Cost must be positive")
    @Column(nullable = false)
    private long cost;

    @Positive(message = "Retail price must be positive")
    @Column(name = "retail_price", nullable = false)
    private long retailPrice;

    @PositiveOrZero(message = "Alert quantity must be zero or positive")
    @Column(name = "alert_quantity")
    private long alertQuantity;

    @Positive(message = "Unit must be positive")
    @Column(nullable = false)
    private long unit;

    @NotBlank(message = "Currency is required")
    @Column(nullable = false)
    private String currency;

    @NotBlank(message = "Tax is required")
    @Column(nullable = false)
    private String tax;

    @NotBlank(message = "Supplier is required")
    @Column(nullable = false)
    private String supplier;

    @NotBlank(message = "Tax method is required")
    @Column(name = "tax_method", nullable = false)
    private String taxMethod;

    @Positive(message = "Supplier cost must be positive")
    @Column(name = "supplier_cost", nullable = false)
    private long supplierCost;

    @Size(max = 500, message = "Details must not exceed 500 characters")
    @Column(name = "details")
    private String details;

    @Size(max = 500, message = "Invoice details must not exceed 500 characters")
    @Column(name = "details_invoice")
    private String detailsInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    @JsonIgnore
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Inventory> inventories = new HashSet<>();


    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<ProductPurchaseOrder> productPurchaseOrders;

    @PrePersist
    protected void onCreate() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }

}
