package com.javastudio.grandmafood.features.core.controllers.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientUpdateDto {

    @NotNull
    @Schema(description = "document type and number in the format type-number, e.g: CC-1002001032")
    @Size(min = 1, max = 20)
    private String document;

    @NotNull
    @Schema(description = "first and last name")
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Schema(description = "phone number in the format of 322-8887799")
    @Pattern(regexp = "\\d{3}-\\d{7}")
    private String phone;

    @NotNull
    @Schema(description = "delivery address")
    @Size(min = 1, max = 255)
    private String deliveryAddress;
}
