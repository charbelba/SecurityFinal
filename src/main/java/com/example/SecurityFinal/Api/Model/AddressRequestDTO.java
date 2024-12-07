package com.example.SecurityFinal.Api.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequestDTO {

    @NotBlank(message = "Street cannot be blank")
    @Size(max = 100, message = "Street must be up to 100 characters")
    private String street;

    @NotBlank(message = "Area cannot be blank")
    @Size(max = 100, message = "Area must be up to 100 characters")
    private String area;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City must be up to 50 characters")
    private String city;

    @NotBlank(message = "PinCode cannot be blank")
    @Size(max = 10, message = "PinCode must be up to 10 characters")
    private String pinCode;

    @NotBlank(message = "Building cannot be blank")
    @Size(max = 100, message = "Building must be up to 100 characters")
    private String building;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country must be up to 50 characters")
    private String country;

    @Size(max = 50, message = "Zone must be up to 50 characters")
    private String zone;

    @Size(max = 50, message = "Stamping Zone must be up to 50 characters")
    private String stampingZone;
}
