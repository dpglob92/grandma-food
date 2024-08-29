package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ClientDocumentTypeUnknownException extends ApplicationException {
    public ClientDocumentTypeUnknownException() {
        super("The type of document is unknown", ExceptionCode.INVALID_INPUT, "UNKNOWN-DOCUMENT-TYPE");
    }
}
