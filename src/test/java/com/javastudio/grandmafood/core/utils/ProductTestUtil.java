package com.javastudio.grandmafood.core.utils;

import com.javastudio.grandmafood.features.core.entities.product.FoodCategory;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class ProductTestUtil {

    public static Product getValidProduct() {
        return Product.builder()
                .uuid(UUID.randomUUID())
                .name("EXAMPLE PRODUCT")
                .description("This is an example product.")
                .foodCategory(FoodCategory.CHICKEN)
                .price(BigDecimal.valueOf(10.99))
                .available(true)
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .deletedAt(null)
                .build();
    }

    public static ProductCreateInput getValidProductCreateInput() {
        return ProductCreateInput.builder()
                .name("Example Product")
                .description("This is an example product.")
                .foodCategory(FoodCategory.CHICKEN)
                .price(BigDecimal.valueOf(10.99))
                .available(true)
                .build();
    }
}
