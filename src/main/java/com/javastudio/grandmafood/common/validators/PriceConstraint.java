package com.javastudio.grandmafood.common.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=PriceConstraintValidator.class)
public @interface PriceConstraint {

    int precision() default 12;

    int scale() default 2;

    String message() default "price must be positive and have a maximum precision and scale";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
