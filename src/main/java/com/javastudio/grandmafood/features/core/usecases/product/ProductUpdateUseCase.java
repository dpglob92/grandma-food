package com.javastudio.grandmafood.features.core.usecases.product;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.database.entities.ProductJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ProductJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.product.IProductUpdateUseCase;
import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;
import com.javastudio.grandmafood.features.errors.ProductNotFoundException;
import com.javastudio.grandmafood.features.errors.ProductUniqueNameException;
import com.javastudio.grandmafood.features.errors.RequestEqualsException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductUpdateUseCase implements IProductUpdateUseCase {

    Logger logger = LoggerFactory.getLogger(ProductUpdateUseCase.class);

    private final ProductJPAEntityRepository repository;
    private final Validator validator;

    public ProductUpdateUseCase(
            ProductJPAEntityRepository repository,
            Validator validator
    ) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void updateById(UUID uuid, ProductCreateInput product) {
        logger.info("Validating product update");
        ValidationUtils.validate(validator, product);

        Optional<ProductJPAEntity> productJPAEntity = repository.findById(uuid);
        if (productJPAEntity.isEmpty()) {
            throw new ProductNotFoundException();
        }

        ProductJPAEntity productToUpdate = productJPAEntity.get();

        if (isProductUnchanged(productToUpdate, product)) {
            throw new RequestEqualsException();
        }

        productToUpdate.setName(product.getName());
        productToUpdate.setFoodCategory(product.getFoodCategory());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setAvailable(product.isAvailable());
        productToUpdate.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

        try {
            repository.save(productToUpdate);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("product.name_unique_constraint")) {
                logger.info("Could not update product as the name already exits");
                throw new ProductUniqueNameException();
            } else {
                // Unknown exception, propagate it
                throw ex;
            }
        }
    }

    private boolean isProductUnchanged(ProductJPAEntity productEntity, ProductCreateInput productInput) {
        return productEntity.getName().equalsIgnoreCase(productInput.getName()) &&
                productEntity.getFoodCategory().equals(productInput.getFoodCategory()) &&
                productEntity.getDescription().equals(productInput.getDescription()) &&
                productEntity.getPrice().compareTo(productInput.getPrice()) == 0 &&
                productEntity.isAvailable() == productInput.isAvailable();
    }
}
