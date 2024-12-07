package com.example.SecurityFinal.Api.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidEnum annotation) {

    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // You can choose to handle null values as needed
        }

        // Check if the value is a valid enum constant
        boolean isValid = enumClass.isAssignableFrom(value.getDeclaringClass());

        // If not valid, add a violation message
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid value for enum: " + value.name())
                    .addConstraintViolation();
        }

        return isValid;
    }
}
