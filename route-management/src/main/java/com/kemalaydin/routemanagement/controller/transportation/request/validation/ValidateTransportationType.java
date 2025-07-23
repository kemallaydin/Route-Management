package com.kemalaydin.routemanagement.controller.transportation.request.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TransportationTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateTransportationType {
    String message() default "Invalid transportation type!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
