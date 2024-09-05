package com.javastudio.grandmafood.features.core.usecases.product;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.database.adapters.ProductAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ProductJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ProductJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.product.IProductCreateUseCase;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;
import com.javastudio.grandmafood.features.errors.ProductUniqueNameException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class ProductCreateUseCase implements IProductCreateUseCase {

    Logger logger = LoggerFactory.getLogger(ProductCreateUseCase.class);

    private final ProductJPAEntityRepository repository;

    private final Validator validator;

    private final ProductAdapter productAdapter;

    public ProductCreateUseCase(
            ProductJPAEntityRepository repository,
            Validator validator,
            ProductAdapter productAdapter
    ) {
        this.repository = repository;
        this.validator = validator;
        this.productAdapter = productAdapter;
    }

    @Override
    public Product create(ProductCreateInput input) {
        logger.info("Validating product create input");
        ValidationUtils.validate(validator, input);

        ProductJPAEntity productJPAEntity = ProductJPAEntity.builder()
                .name(input.getName().toUpperCase())
                .description(input.getDescription())
                .foodCategory(input.getFoodCategory())
                .price(input.getPrice())
                .available(input.isAvailable())
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();

        try {
            ProductJPAEntity saved = repository.save(productJPAEntity);
            return productAdapter.jpaEntityToDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("product.name_unique_constraint")) {
                logger.info("Could not create product as the name already exits");
                throw new ProductUniqueNameException();
            }else {
                // Unknown exception, propagate it
                throw ex;
            }
        }

    }

}
