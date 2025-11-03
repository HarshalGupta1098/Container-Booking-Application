package com.shipping.booking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ContainerSizeValidator 
        implements ConstraintValidator<ContainerSizeConstraint, Integer> {

    private static final List<Integer> VALID_SIZES = Arrays.asList(20, 40);

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return VALID_SIZES.contains(value);
    }
}
