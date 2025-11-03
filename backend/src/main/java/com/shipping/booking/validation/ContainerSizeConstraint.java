package com.shipping.booking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContainerSizeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContainerSizeConstraint {
    String message() default "Container size must be either 20 or 40";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}