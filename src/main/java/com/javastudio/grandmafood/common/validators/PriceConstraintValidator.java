package com.javastudio.grandmafood.common.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PriceConstraintValidator implements ConstraintValidator<PriceConstraint, BigDecimal> {

    private int precision;

    private int scale;

    @Override
    public void initialize(PriceConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.precision = constraintAnnotation.precision();
        this.scale = constraintAnnotation.scale();
        this.validateParameters();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        return (value.compareTo(BigDecimal.ZERO) > 0)
                & (value.scale() <= this.scale)
                & (value.precision() <= this.precision);
    }

    private void validateParameters() {
        if (this.precision <= 0) {
            throw new IllegalArgumentException("precision cannot be negative or zero");
        }

        if (this.scale < 0) {
            throw new IllegalArgumentException("scale cannot be negative");
        }
    }
}
