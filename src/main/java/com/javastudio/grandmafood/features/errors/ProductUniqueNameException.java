package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ProductUniqueNameException extends ApplicationException {

    public ProductUniqueNameException() {
        super("product name must be unique", ExceptionCode.DUPLICATED_RECORD, "PRODUCT-UNIQUE-NAME");
    }
}
