package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ClientUniqueDocumentException extends ApplicationException {

    public ClientUniqueDocumentException() {
        super("client document must be unique", ExceptionCode.DUPLICATED_RECORD, "CLIENT-UNIQUE-DOCUMENT");
    }
}
