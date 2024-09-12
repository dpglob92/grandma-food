package com.javastudio.grandmafood.core.integration.client;

import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.core.utils.CustomAssertions;
import com.javastudio.grandmafood.features.core.usecases.client.ClientCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
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
            Assertions.assertThat(client.getDeletedAt()).isNull();
        } else {
            Assertions.fail("client not found");
        }

    }
}
