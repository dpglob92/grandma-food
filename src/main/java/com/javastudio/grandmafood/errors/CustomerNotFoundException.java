package com.javastudio.grandmafood.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class CustomerNotFoundException extends ApplicationException {

    public CustomerNotFoundException() {
        super("customer was not found", ExceptionCode.NOT_FOUND, "CUSTOMER-NOT-FOUND");
    }
}
