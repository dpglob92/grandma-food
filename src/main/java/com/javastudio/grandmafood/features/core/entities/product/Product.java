package com.javastudio.grandmafood.features.core.entities.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {

    @NonNull
    private UUID uuid;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private FoodCategory foodCategory;

    @NonNull
    private BigDecimal price;

    private boolean available;

    @NonNull
    private LocalDateTime createdAt;

    @NonNull
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
