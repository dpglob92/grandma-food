package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ParameterNullException extends ApplicationException {
    public ParameterNullException(String parameter) {
        super("Parameter " + parameter + " should not be null", ExceptionCode.INVALID_INPUT, "PARAMETER-NULL");
    }
}
