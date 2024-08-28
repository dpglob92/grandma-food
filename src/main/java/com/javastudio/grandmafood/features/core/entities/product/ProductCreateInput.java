package com.javastudio.grandmafood.features.core.entities.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductCreateInput {

    @NotNull
    @Size(min=1, max=255, message = "the name must have at most 255 characters and cannot be empty")
    private String name;

    @NotNull
    @Size(max=511, message = "the description must have at most 511 characters")
    private String description;

    @NotNull
    private FoodCategory foodCategory;

    @NotNull
    @Positive
    private BigDecimal price;

    private boolean available;
}
