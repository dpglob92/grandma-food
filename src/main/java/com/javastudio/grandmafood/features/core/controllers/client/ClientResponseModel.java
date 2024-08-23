package com.javastudio.grandmafood.features.core.controllers.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientResponseModel {

    @Schema(description = "document type and number in the format type-number, e.g: CC-1002001032")
    private String document;

    @Schema(description = "first and last name")
    private String name;

    private String email;

    @Schema(description = "phone number in the format of 322-8887799")
    private String phone;

    @Schema(description = "delivery address")
    private String deliveryAddress;
}
