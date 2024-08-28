package com.javastudio.grandmafood.features.core.usecases.product;

import com.javastudio.grandmafood.features.core.database.repositories.ProductJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.product.IProductCreateUseCase;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;
import jakarta.validation.Validator;

public class ProductCreateUseCase implements IProductCreateUseCase {

    private final ProductJPAEntityRepository repository;

    private final Validator validator;

    public ProductCreateUseCase(ProductJPAEntityRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Product create(ProductCreateInput input) {
        return null;
    }
}
