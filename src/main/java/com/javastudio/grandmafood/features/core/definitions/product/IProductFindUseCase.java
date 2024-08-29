package com.javastudio.grandmafood.features.core.definitions.product;

import com.javastudio.grandmafood.features.core.entities.product.Product;

import java.util.Optional;
import java.util.UUID;

public interface IProductFindUseCase {

    Optional<Product> findById(UUID uuid);
}
