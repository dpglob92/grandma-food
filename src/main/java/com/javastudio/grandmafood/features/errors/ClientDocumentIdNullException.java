package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ClientDocumentIdNullException extends ApplicationException {
    public ClientDocumentIdNullException() {
        super("documentId was not provided in the header", ExceptionCode.ID_NOT_PROVIDED, "DOCUMENT-ID-NOT-PROVIDED");
    }
}
