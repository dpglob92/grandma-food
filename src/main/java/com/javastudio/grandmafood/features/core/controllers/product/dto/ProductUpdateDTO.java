package com.javastudio.grandmafood.features.core.controllers.product.dto;

import com.javastudio.grandmafood.features.core.entities.product.FoodCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateDTO {
    @NotNull
    @Size(min=1, max=255)
    private String fantasyName;

    @NotNull
    @Size(max=511)
    private String description;

    @NotNull
    private FoodCategory foodCategory;

    @NotNull
    @Schema(description = "price without iva, must be positive, only 2 decimal places")
    private String price;

    private boolean available;
}
