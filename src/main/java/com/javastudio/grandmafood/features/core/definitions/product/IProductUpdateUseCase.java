package com.javastudio.grandmafood.features.core.definitions.product;

import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;

import java.util.UUID;

public interface IProductUpdateUseCase {
    void updateById(UUID uuid, ProductCreateInput product);
}
