package com.javastudio.grandmafood.common.exceptions;

import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;


public class ValidationUtils {

    static Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    public static <T> void validate(Validator validator, T input) {
        logger.debug("Validating input");
        var valResult = validator.validate(input);
        if (!valResult.isEmpty()) {
            logger.debug("Validation failed");
            InvalidInputException ex = new InvalidInputException();
            valResult.forEach(vr -> ex.addError(vr.getPropertyPath().toString(), vr.getMessage()));
            throw ex;
        }
    }

    public static UUID parseUUID(String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            InvalidInputException ex = new InvalidInputException();
            ex.addError("uuid", "invalid uuid");
            throw ex;
        }
    }

    public static LocalDateTime parseToLocalDateTime(String dateString, String fieldName) {
        try {
            return LocalDateTime.parse(dateString);
        } catch (Exception e) {
            InvalidInputException ex = new InvalidInputException();
            ex.addError(fieldName, "invalid date");
            throw ex;
        }
    }

}
