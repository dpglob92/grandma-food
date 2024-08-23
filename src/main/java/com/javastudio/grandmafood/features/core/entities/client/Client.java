package com.javastudio.grandmafood.features.core.entities.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Client {

    @NonNull
    private UUID uuid;

    @NonNull
    private String documentId;

    @NonNull
    private DocumentType documentType;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String phone;

    @NonNull
    private String deliveryAddress;

    @NonNull
    private LocalDateTime createdAt;

    @NonNull
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
