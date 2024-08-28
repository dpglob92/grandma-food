package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ClientInvalidDocumentFormatException extends ApplicationException {
    public ClientInvalidDocumentFormatException() {
        super("Invalid document format. Expected format 'TYPE-NUMBER'.", ExceptionCode.INVALID_INPUT, "INVALID-DOCUMENT-FORMAT");
    }
}
