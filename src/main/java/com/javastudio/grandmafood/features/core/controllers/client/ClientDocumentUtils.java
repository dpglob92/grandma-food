package com.javastudio.grandmafood.features.core.controllers.client;

import com.javastudio.grandmafood.features.core.entities.client.DocumentType;
import com.javastudio.grandmafood.features.errors.ClientDocumentTypeUnknownException;
import com.javastudio.grandmafood.features.errors.ClientInvalidDocumentFormatException;

public class ClientDocumentUtils {
    // method to separate the document into type and number
    public static DocumentData separateDocument(String document) {
        if (document == null) {
            throw new ClientInvalidDocumentFormatException();
        }

        String[] documentParts = document.split("-", 2);
        if (documentParts.length != 2) {
            throw new ClientInvalidDocumentFormatException();
        }
        DocumentType documentType = mapToDocumentType(documentParts[0]);
        String documentId = documentParts[1];
        return new DocumentData(documentType, documentId);
    }

    // method to concatenate documentType and documentId
    public static String concatenateDocument(DocumentType documentType, String documentId) {
        return documentType.name() + "-" + documentId;
    }

    // method to map the document type code to the corresponding enum
    private static DocumentType mapToDocumentType(String documentTypeCode) {
        return switch (documentTypeCode) {
            case "CC" -> DocumentType.CC;
            case "TI" -> DocumentType.TI;
            case "TE" -> DocumentType.TE;
            default -> throw new ClientDocumentTypeUnknownException();
        };
    }

    // Store data class separate from the document
    public record DocumentData(DocumentType documentType, String documentId) {

    }
}
