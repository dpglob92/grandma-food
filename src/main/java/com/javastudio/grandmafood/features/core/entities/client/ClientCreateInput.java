package com.javastudio.grandmafood.features.core.entities.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientCreateInput {

    @NotNull
    @Size(min = 1, max = 15, message = "the documentId must have at most 25 characters and cannot be empty")
    private String documentId;

    @NotNull
    private DocumentType documentType;

    @NotNull
    @Size(min = 1, max = 255, message = "the name must have at most 255 characters and cannot be empty")
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Size(min = 1, max = 11, message = "the phone must have at most 11 characters and cannot be empty")
    private String phone;

    @NotNull
    @Size(min = 1, max = 500, message = "the delivery address must have at most 500 characters and cannot be empty")
    private String deliveryAddress;
}
