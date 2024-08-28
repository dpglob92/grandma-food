package com.javastudio.grandmafood.features.core.usecases.product;

import com.javastudio.grandmafood.features.core.database.repositories.ProductJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.product.IProductFindUseCase;
import com.javastudio.grandmafood.features.core.entities.product.Product;

import java.util.Optional;
import java.util.UUID;

public class ProductFindUseCase implements IProductFindUseCase {

    private final ProductJPAEntityRepository repository;

    public ProductFindUseCase(ProductJPAEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.empty();
    }
}
