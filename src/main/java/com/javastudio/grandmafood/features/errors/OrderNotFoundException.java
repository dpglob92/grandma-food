package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class OrderNotFoundException extends ApplicationException {

    public OrderNotFoundException() {
        super("order not found", ExceptionCode.NOT_FOUND, "ORDER-NOT-FOUND");
    }
}
