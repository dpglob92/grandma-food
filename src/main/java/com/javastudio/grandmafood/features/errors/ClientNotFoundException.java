package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ClientNotFoundException extends ApplicationException {

    public ClientNotFoundException() {
        super("client was not found", ExceptionCode.NOT_FOUND, "CLIENT-NOT-FOUND");
    }
}
