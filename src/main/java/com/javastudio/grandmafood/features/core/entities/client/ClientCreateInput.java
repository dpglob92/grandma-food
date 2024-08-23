package com.javastudio.grandmafood.features.core.entities.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientCreateInput {

    private String documentId;

    private DocumentType documentType;

    private String name;

    private String email;

    private String phone;

    private String deliveryAddress;
}
