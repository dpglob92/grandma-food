package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ClientUniqueEmailException extends ApplicationException {

    public ClientUniqueEmailException() {
        super("client email must be unique", ExceptionCode.DUPLICATED_RECORD, "CLIENT-UNIQUE-EMAIL");
    }
}
