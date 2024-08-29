package com.javastudio.grandmafood.common.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.javastudio.grandmafood.common.exceptions.FieldError;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class represents the common error response structure for the API
 */
@Data
public class ApiError {

    private final String path;
    @JsonProperty("code")
    private final String errorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final LocalDateTime timestamp;
    @JsonProperty("description")
    private String message;
    @JsonProperty("exception")
    private String debugMessage;
    private List<FieldError> fieldErrors;

    ApiError(String path, String errorCode) {
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.errorCode = errorCode;
        this.message = "unexpected error";
        this.debugMessage = "unexpected error";
        this.fieldErrors = List.of();
    }

    ApiError(String path, String errorCode, Throwable ex){
        this(path, errorCode);
        this.message = "unexpected error";
        this.debugMessage = ex.getClass().getSimpleName();
    }

    ApiError(String path, String message, String errorCode, Throwable ex) {
        this(path, errorCode);
        this.message = message;
        this.debugMessage = ex.getClass().getSimpleName();
    }

    ApiError(String path, String message, String errorCode, Throwable ex, List<FieldError> fieldErrors) {
        this(path, errorCode);
        this.message = message;
        this.debugMessage = ex.getClass().getSimpleName();
        this.fieldErrors = fieldErrors;
    }
}
