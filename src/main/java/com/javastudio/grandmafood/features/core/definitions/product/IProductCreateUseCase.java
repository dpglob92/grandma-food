package com.javastudio.grandmafood.features.core.definitions.product;

import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;

public interface IProductCreateUseCase {

    Product create(ProductCreateInput input);
}
