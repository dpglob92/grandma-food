package com.javastudio.grandmafood.features.core.controllers.product.dto;

import com.javastudio.grandmafood.features.core.entities.product.FoodCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductDTO {

    private UUID uuid;

    private String fantasyName;

    private String description;

    private FoodCategory foodCategory;

    private String price;

    private boolean available;
}
