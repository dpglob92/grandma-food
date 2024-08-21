package com.javastudio.grandmafood.common.web;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApiError> handleApplicationException(HttpServletRequest req, ApplicationException ex) {

        HttpStatus statusCode = ErrorCodeUtils.getHttpStatusFromExceptionCode(ex.getExceptionCode());
        String userMessage = ex.getMessage();
        String path = req.getRequestURI();
        String errorCode = ex.getFullErrorCode();

        ApiError apiError = new ApiError(path, userMessage, errorCode, ex);

        logger.debug(
                "an application exception occurred with code: {} and message: {}",
                ex.getFullErrorCode(),
                ex.getMessage()
        );
        return ResponseEntity.status(statusCode).body(apiError);
    }

    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<ApiError> handleInvalidInputException(HttpServletRequest req, InvalidInputException ex) {

        HttpStatus statusCode = ErrorCodeUtils.getHttpStatusFromExceptionCode(ex.getExceptionCode());
        String userMessage = ex.getMessage();
        List<FieldError> fieldErrors = ex.getErrors();
        String path = req.getRequestURI();
        String errorCode = ex.getFullErrorCode();

        ApiError apiError = new ApiError(path, userMessage, errorCode, ex, fieldErrors);

        logger.debug(
                "an invalid input exception occurred with code: {} and message: {}",
                errorCode,
                ex.getMessage()
        );
        return ResponseEntity.status(statusCode).body(apiError);
    }
}
