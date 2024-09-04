package com.javastudio.grandmafood.features.core.entities.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderCreateInput {

    @NotNull
    @Size(max = 511, message = "the additional information must have at most 511 characters and cannot be empty")
    private String additionalInformation;

    @NotNull
    @Size(min=1, max=150, message="you can only order a maximum of 150 products and a minimum of 1")
    private List<@Valid OrderItemCreateInput> orderItems;

    @NotNull
    private String clientDocumentId;
}
