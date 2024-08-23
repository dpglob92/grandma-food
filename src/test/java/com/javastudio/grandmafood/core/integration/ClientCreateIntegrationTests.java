package com.javastudio.grandmafood.core.integration;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.core.utils.CustomAssertions;
import com.javastudio.grandmafood.features.core.usecases.ClientCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.ClientFindUseCase;
import com.javastudio.grandmafood.features.errors.ClientUniqueDocumentException;
import com.javastudio.grandmafood.features.errors.ClientUniqueEmailException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientCreateIntegrationTests {

    @Autowired
    private ClientCreateUseCase clientCreateUseCase;

    @Autowired
    private ClientFindUseCase clientFindUseCase;

    @Test
    public void Should_CreateAndPersistClient_WhenDataIsValid() {
        var validInput = ClientTestUtil.getValidClientCreateInput();
        var createdClient = clientCreateUseCase.create(validInput);

        var retrievedClient = clientFindUseCase.findById(createdClient.getUuid());

        if (retrievedClient.isPresent()) {
            var client = retrievedClient.get();
            Assertions.assertThat(client.getDocumentId()).isEqualTo(validInput.getDocumentId());
            Assertions.assertThat(client.getDocumentType()).isEqualTo(validInput.getDocumentType());
            Assertions.assertThat(client.getName()).isEqualTo(validInput.getName());
            Assertions.assertThat(client.getEmail()).isEqualTo(validInput.getEmail());
            Assertions.assertThat(client.getPhone()).isEqualTo(validInput.getPhone());
            Assertions.assertThat(client.getDeliveryAddress()).isEqualTo(validInput.getDeliveryAddress());

            CustomAssertions.assertDateTimeIsCloseToNow(client.getCreatedAt(), 5);
            CustomAssertions.assertDateTimeIsCloseToNow(client.getUpdatedAt(), 5);
            Assertions.assertThat(client.getDeletedAt()).isEqualTo(null);
        } else {
            Assertions.fail("client not found");
        }

    }

    @Test
    public void Should_ThrowUniqueDocumentException_WhenDocumentIsDuplicated() {
        var validInput = ClientTestUtil.getValidClientCreateInput();
        var duplicatedInput = validInput.toBuilder().email("jjkk@gmail.com").build();

        clientCreateUseCase.create(validInput);

        Assertions.assertThatThrownBy(() -> clientCreateUseCase.create(duplicatedInput))
                .isInstanceOf(ClientUniqueDocumentException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.DUPLICATED_RECORD);

        clientCreateUseCase.create(validInput);

    }

    @Test
    public void Should_ThrowUniqueEmailException_WhenDocumentIsDuplicated() {
        var validInput = ClientTestUtil.getValidClientCreateInput();
        var duplicatedInput = validInput.toBuilder().documentId("100200300400").build();

        clientCreateUseCase.create(validInput);

        Assertions.assertThatThrownBy(() -> clientCreateUseCase.create(duplicatedInput))
                .isInstanceOf(ClientUniqueEmailException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.DUPLICATED_RECORD);

        clientCreateUseCase.create(validInput);

    }
}
