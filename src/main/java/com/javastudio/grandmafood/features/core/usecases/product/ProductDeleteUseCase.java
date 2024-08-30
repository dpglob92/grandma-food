package com.javastudio.grandmafood.features.core.usecases.product;

import com.javastudio.grandmafood.features.core.database.entities.ProductJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ProductJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.product.IProductDeleteUseCase;
import com.javastudio.grandmafood.features.errors.ProductNotFoundException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductDeleteUseCase implements IProductDeleteUseCase {

    Logger logger = LoggerFactory.getLogger(ProductDeleteUseCase.class);

    private final ProductJPAEntityRepository repository;

    private final Validator validator;

    public ProductDeleteUseCase(ProductJPAEntityRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void deleteById(UUID uuid) {
        validator.validate(uuid);

        logger.info("finding product with uuid: {}", uuid);
        Optional<ProductJPAEntity> product = repository.findById(uuid);
        if (product.isEmpty()) {
            throw new ProductNotFoundException();
        }

        repository.delete(product.get());
        logger.info("product deleted with uuid: {}", uuid);
    }
}
