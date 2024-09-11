package com.javastudio.grandmafood.core.unit.client;

import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.features.core.controllers.client.dto.ClientCreateDTO;
import com.javastudio.grandmafood.features.core.controllers.client.dto.ClientDTOMapper;
import com.javastudio.grandmafood.features.core.entities.client.DocumentType;
import com.javastudio.grandmafood.features.errors.ClientDocumentTypeUnknownException;
import com.javastudio.grandmafood.features.errors.ClientInvalidDocumentFormatException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class ClientDTOMapperTest {

    private final ClientDTOMapper clientDTOMapper;

    public ClientDTOMapperTest() {
        this.clientDTOMapper = new ClientDTOMapper();
    }

    @ParameterizedTest
    @MethodSource("getValidDocuments")
    public void Should_MapToCreateInputData_WhenDataIsValid(
            String realDocument,
            DocumentType expectedDocumentType,
            String documentId
    ) {
        var data = getValidCreateDTO();
        data.setDocument(realDocument);
        var input = clientDTOMapper.toDomain(data);

        // we are only interested in those fields who contain some business logic
        Assertions.assertThat(input.getDocumentId()).isEqualTo(documentId);
        Assertions.assertThat(input.getDocumentType()).isEqualTo(expectedDocumentType);
    }

    @Test
    public void Should_MapToClientDTO_WhenDataIsValid() {
        var client = ClientTestUtil.getValidClient();
        client.setDocumentId("123456789");
        client.setDocumentType(DocumentType.CC);

        var clientDTO = clientDTOMapper.domainToDto(client);

        Assertions.assertThat(clientDTO.getDocument()).isEqualTo("CC-123456789");
    }

    @Test
    public void Should_ThrowClientInvalidDocumentFormatException_WhenCreateDTODocumentIsNull() {
        var data = getValidCreateDTO();
        data.setDocument(null);

        assertInvalidDocument(() -> clientDTOMapper.toDomain(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"KKK22234", "", "100casa"})
    public void Should_ThrowClientInvalidDocumentFormatException_WhenCreateDTODocumentIsInvalid(String realDocument) {
        var data = getValidCreateDTO();
        data.setDocument(realDocument);

        assertInvalidDocument(() -> clientDTOMapper.toDomain(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TU-120DAS11", "CASA-120DAS11", "OP-100200300", "-HOT"})
    public void Should_ThrowClientDocumentTypeUnknownException_WhenCreateDTODocumentTypeIsNotFound(String realDocument) {
        var data = getValidCreateDTO();
        data.setDocument(realDocument);

        assertInvalidDocumentType(() -> clientDTOMapper.toDomain(data));
    }

    private void assertInvalidDocument(ThrowableAssert.ThrowingCallable callMapperMethod) {
        ClientInvalidDocumentFormatException throwable = Assertions.catchThrowableOfType(
                callMapperMethod,
                ClientInvalidDocumentFormatException.class
        );
        Assertions.assertThat(throwable).withFailMessage("ClientInvalidDocumentFormatException was not thrown").isNotNull();
    }

    private void assertInvalidDocumentType(ThrowableAssert.ThrowingCallable callMapperMethod) {
        ClientDocumentTypeUnknownException throwable = Assertions.catchThrowableOfType(
                callMapperMethod,
                ClientDocumentTypeUnknownException.class
        );
        Assertions.assertThat(throwable).withFailMessage("ClientDocumentTypeUnknownException was not thrown").isNotNull();
    }

    private ClientCreateDTO getValidCreateDTO() {
        return ClientCreateDTO.builder()
                .document("CC-123456789")
                .name("John Doe")
                .email("johndoe@example.com")
                .phone("322-5006070")
                .deliveryAddress("123 Main St, City, State")
                .build();
    }

    private static Stream<Arguments> getValidDocuments() {
        return Stream.of(
                Arguments.of("CC-100200400", DocumentType.CC, "100200400"),
                Arguments.of("TI-5002001", DocumentType.TI, "5002001"),
                Arguments.of("TE-3601444", DocumentType.TE, "3601444"),
                // documentId can also be alphanumeric
                Arguments.of("CC-100abc300", DocumentType.CC, "100abc300")
        );
    }
}

