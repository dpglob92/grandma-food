package com.javastudio.grandmafood.common.web;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import org.springframework.http.HttpStatus;

/**
 * This class maps general exception codes to HTTP status codes
 */
public class ErrorCodeUtils {

    public static HttpStatus getHttpStatusFromExceptionCode(ExceptionCode exceptionCode) {
        return switch (exceptionCode) {
            case INVALID_INPUT, INVALID_ID, ID_NOT_PROVIDED -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATED_RECORD, INVALID_OPERATION -> HttpStatus.CONFLICT;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
