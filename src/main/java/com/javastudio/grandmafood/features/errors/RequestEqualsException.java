package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class RequestEqualsException extends ApplicationException {
    public RequestEqualsException() {
        super("there are no different fields in the request", ExceptionCode.DUPLICATED_RECORD, "REQUEST-EQUAL");
    }
}