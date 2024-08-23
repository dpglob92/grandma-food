package com.javastudio.grandmafood.features.core.database.adapters;

import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.entities.client.Client;

public class ClientAdapter {

    public static Client jpaEntityToDomain(ClientJPAEntity clientJPAEntity) {
        return Client.builder()
                .uuid(clientJPAEntity.getId())
                .documentId(clientJPAEntity.getDocumentId())
                .documentType(clientJPAEntity.getDocumentType())
                .name(clientJPAEntity.getName())
                .email(clientJPAEntity.getEmail())
                .phone(clientJPAEntity.getPhone())
                .deliveryAddress(clientJPAEntity.getDeliveryAddress())
                .createdAt(clientJPAEntity.getCreatedAt())
                .updatedAt(clientJPAEntity.getUpdatedAt())
                .deletedAt(clientJPAEntity.getDeletedAt())
                .build();
    }

    public static ClientJPAEntity domainToJPAEntity(Client client) {
        return ClientJPAEntity.builder()
                .id(client.getUuid())
                .documentId(client.getDocumentId())
                .documentType(client.getDocumentType())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .deliveryAddress(client.getDeliveryAddress())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .deletedAt(client.getDeletedAt())
                .build();
    }
}
