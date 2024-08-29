package com.javastudio.grandmafood.features.core.usecases.product;

import com.javastudio.grandmafood.features.core.database.adapters.ProductAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ProductJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ProductJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.product.IProductFindUseCase;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductFindUseCase implements IProductFindUseCase {

    Logger logger = LoggerFactory.getLogger(ProductFindUseCase.class);

    private final ProductJPAEntityRepository repository;

    private final ProductAdapter productAdapter;

    public ProductFindUseCase(ProductJPAEntityRepository repository, ProductAdapter productAdapter) {
        this.repository = repository;
        this.productAdapter = productAdapter;
    }

    @Override
    public Optional<Product> findById(UUID uuid) {
        logger.info("Retrieving actor with id: {}", uuid);
        Optional<ProductJPAEntity> productJPAEntity = repository.findById(uuid);

        return productJPAEntity.map(productAdapter::jpaEntityToDomain);

    }
}
