package com.kemalaydin.routemanagement.controller.transportation.request.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TransportationOperatingDaysValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateTransportationOperatingDays {
    String message() default "Operating days must be integers between 1 and 7!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
