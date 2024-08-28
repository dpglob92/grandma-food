package com.javastudio.grandmafood.features.core.database.adapters;

import com.javastudio.grandmafood.features.core.database.entities.ProductJPAEntity;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductAdapter {

    public Product jpaEntityToDomain(ProductJPAEntity productJPAEntity) {
        return Product.builder()
                .uuid(productJPAEntity.getId())
                .name(productJPAEntity.getName())
                .description(productJPAEntity.getDescription())
                .foodCategory(productJPAEntity.getFoodCategory())
                .price(productJPAEntity.getPrice())
                .available(productJPAEntity.isAvailable())
                .createdAt(productJPAEntity.getCreatedAt())
                .updatedAt(productJPAEntity.getUpdatedAt())
                .deletedAt(productJPAEntity.getDeletedAt())
                .build();
    }

    public ProductJPAEntity domainToJPAEntity(Product product) {
        return ProductJPAEntity.builder()
                .id(product.getUuid())
                .name(product.getName())
                .description(product.getDescription())
                .foodCategory(product.getFoodCategory())
                .price(product.getPrice())
                .available(product.isAvailable())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .deletedAt(product.getDeletedAt())
                .build();
    }
}
