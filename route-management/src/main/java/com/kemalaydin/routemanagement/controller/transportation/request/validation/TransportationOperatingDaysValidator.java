package com.kemalaydin.routemanagement.controller.transportation.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class TransportationOperatingDaysValidator implements ConstraintValidator<ValidateTransportationOperatingDays, Set<Integer>> {

    @Override
    public boolean isValid(Set<Integer> days, ConstraintValidatorContext context) {
        if (CollectionUtils.isEmpty(days)) {
            return false;
        }

        return days.stream().allMatch(day -> day >= 1 && day <= 7);
    }
}
