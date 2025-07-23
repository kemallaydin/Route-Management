package com.kemalaydin.routemanagement.controller.transportation.request.validation;

import com.kemalaydin.routemanagement.model.transportation.TransportationType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class TransportationTypeValidator implements ConstraintValidator<ValidateTransportationType, String> {

    @Override
    public boolean isValid(String type, ConstraintValidatorContext context) {
        return StringUtils.hasText(type) && Arrays.stream(TransportationType.values())
                .anyMatch(enumValue -> enumValue.name().equalsIgnoreCase(type));
    }
}
