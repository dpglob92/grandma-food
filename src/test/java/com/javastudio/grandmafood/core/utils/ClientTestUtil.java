package com.javastudio.grandmafood.core.utils;

import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import com.javastudio.grandmafood.features.core.entities.client.ClientUpdateInput;
import com.javastudio.grandmafood.features.core.entities.client.DocumentType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class ClientTestUtil {

    public static Client getValidClient() {
        return Client.builder()
                .uuid(UUID.randomUUID())
                .documentId("123456789")
                .documentType(DocumentType.CC)
                .name("John Doe")
                .email("johndoe@example.com")
                .phone("322-5006070")
                .deliveryAddress("123 Main St, City, State")
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .deletedAt(null)
                .build();
    }

    public static ClientCreateInput getValidClientCreateInput() {
        return ClientCreateInput.builder()
                .documentId("123456789")
                .documentType(DocumentType.CC)
                .name("John Doe")
                .email("johndoe@example.com")
                .phone("322-5006070")
                .deliveryAddress("123 Main St, City, State")
                .build();
    }

    public static ClientUpdateInput getValidClientUpdateInput() {
        return ClientUpdateInput.builder()
                .documentId("123456789")
                .documentType(DocumentType.CC)
                .name("John Doe")
                .email("johndoe@example.com")
                .phone("322-5006070")
                .deliveryAddress("123 Main St, City, State")
                .build();
    }
}
