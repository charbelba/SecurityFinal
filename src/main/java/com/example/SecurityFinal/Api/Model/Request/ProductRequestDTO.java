package com.example.SecurityFinal.Api.Model.Request;

import com.example.SecurityFinal.Api.Model.ProductType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;


@Data
public class ProductRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String icon;

    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @NotNull(message = "Product type is required")
    private ProductType type;

    @FutureOrPresent(message = "Expiry date must be in the present or future")
    @NotNull(message = "expiryDate is required")
    private LocalDate expiryDate;

    @Positive(message = "Cost must be positive")
    private long cost;

    @Positive(message = "Retail price must be positive")
    private long retailPrice;

    @PositiveOrZero(message = "Alert quantity must be zero or positive")
    private long alertQuantity;

    @Positive(message = "Unit must be positive")
    private long unit;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Tax is required")
    private String tax;

    @NotBlank(message = "Supplier is required")
    private String supplier;

    @NotBlank(message = "Tax method is required")
    private String taxMethod;

    @Positive(message = "Supplier cost must be positive")
    private long supplierCost;

    @Size(max = 500, message = "Details must not exceed 500 characters")
    private String details;

    @Size(max = 500, message = "Invoice details must not exceed 500 characters")
    private String detailsInvoice;

}
