package com.javastudio.grandmafood.features.core.controllers.client;

import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import org.springframework.stereotype.Component;

@Component
public class ClientDTOMapper {

    public ClientCreateInput toDomain(ClientCreateDTO data) {
        ClientDocumentUtils.DocumentData documentData = ClientDocumentUtils.separateDocument(data.getDocument());
        return ClientCreateInput.builder()
                .documentType(documentData.documentType())
                .documentId(documentData.documentId())
                .name(data.getName())
                .phone(data.getPhone())
                .email(data.getEmail())
                .deliveryAddress(data.getDeliveryAddress())
                .build();
    }

    public ClientDTO domainToDto(Client client) {
        String document = ClientDocumentUtils.concatenateDocument(client.getDocumentType(), client.getDocumentId());
        return ClientDTO.builder()
                .document(document)
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .deliveryAddress(client.getDeliveryAddress())
                .build();
    }


}
