package com.javastudio.grandmafood.features.core.entities.orders;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderItemCreateInput {

    @NotNull
    private UUID productId;

    @NotNull
    @Positive(message="you can only buy a minimum of 1 unit of a particular product")
    @Max(value = 100, message="you can only buy a maximum of 100 units of a particular product")
    private Integer quantity;
}
