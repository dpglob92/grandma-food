package com.javastudio.grandmafood.features.errors;

import com.javastudio.grandmafood.common.exceptions.ApplicationException;
import com.javastudio.grandmafood.common.exceptions.ExceptionCode;

public class ProductNotFoundException extends ApplicationException {

    public ProductNotFoundException() {
        super("product not found", ExceptionCode.NOT_FOUND, "PRODUCT-NOT-FOUND");
    }

    public ProductNotFoundException(String id) {
        super(String.format("product with %s not found", id), ExceptionCode.NOT_FOUND, "PRODUCT-NOT-FOUND");
    }
}
