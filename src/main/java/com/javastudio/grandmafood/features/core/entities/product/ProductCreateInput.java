package com.javastudio.grandmafood.features.core.entities.product;

import com.javastudio.grandmafood.common.validators.PriceConstraint;
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
    @PriceConstraint(
            precision = 10,
            scale = 2,
            message = "price must be positive and have a maximum precision of 10 and 2 decimal places"
    )
    private BigDecimal price;

    private boolean available;
}
