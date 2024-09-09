package com.javastudio.grandmafood.features.core.controllers.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderCreateDTO {

    @NotNull
    @Schema(description = "document type and number in the format type-number, e.g: CC-1002001032")
    @Size(min = 1, max = 20)
    private String clientDocument;

    @NotNull
    private String productUuid;

    @NotNull
    @Positive()
    @Max(value = 100)
    private Integer quantity;

    @NotNull
    @Size(max = 511)
    private String additionalInformation;
}
