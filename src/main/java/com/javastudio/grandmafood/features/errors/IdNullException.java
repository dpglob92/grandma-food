package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class IdNullException extends ApplicationException {
    public IdNullException() {
        super("id was not provided in the header", ExceptionCode.ID_NOT_PROVIDED, "ID-NOT-PROVIDED");
    }
}
