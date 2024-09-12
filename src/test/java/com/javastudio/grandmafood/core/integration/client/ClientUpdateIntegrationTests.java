package com.javastudio.grandmafood.core.integration.client;

import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.usecases.client.ClientCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientUpdateUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import com.javastudio.grandmafood.features.errors.RequestEqualsException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientUpdateIntegrationTests {

    @Autowired
    private ClientCreateUseCase clientCreateUseCase;

    @Autowired
    private ClientFindUseCase clientFindUseCase;

    @Autowired
    private ClientUpdateUseCase clientUpdateUseCase;

    private Client client1;
    private Client client2;

    @BeforeEach
    public void setup() {

        var validInput1 = ClientTestUtil.getValidClientCreateInput();
        this.client1 = clientCreateUseCase.create(validInput1);
        var validInput2 = ClientTestUtil.getValidClientCreateInput().toBuilder()
                .documentId("1005004001")
                .name("Jul Kon")
                .email("julkon@ema.com")
                .build();
        this.client2 = clientCreateUseCase.create(validInput2);
    }

    @Test
    public void Should_ThrowClientUniqueEmailException_WhenEmailIsDuplicated() {
        var updateInput = ClientTestUtil.getValidClientUpdateInput().toBuilder()
                .email(client2.getEmail())
                .build();
        Assertions.assertThatThrownBy(() -> clientUpdateUseCase.updateByDocument(client1.getDocumentId(), updateInput))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void Should_ThrowClientNotFoundException_WhenClientDoesNotExist() {
        var updateInput = ClientTestUtil.getValidClientUpdateInput();
        Assertions.assertThatThrownBy(() -> clientUpdateUseCase.updateByDocument("non-existing-doc-id", updateInput))
                .isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenInputDataIsInvalid() {
        var invalidUpdateInput = ClientTestUtil.getValidClientUpdateInput().toBuilder()
                .email("invalid-email-format")
                .build();
        Assertions.assertThatThrownBy(() -> clientUpdateUseCase.updateByDocument(client1.getDocumentId(), invalidUpdateInput))
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    public void Should_ThrowRequestEqualsException_WhenNoFieldsAreChanged() {
        var noChangesInput = ClientTestUtil.getValidClientUpdateInput().toBuilder()
                .name(client1.getName())
                .email(client1.getEmail())
                .phone(client1.getPhone())
                .deliveryAddress(client1.getDeliveryAddress())
                .build();
        Assertions.assertThatThrownBy(() -> clientUpdateUseCase.updateByDocument(client1.getDocumentId(), noChangesInput))
                .isInstanceOf(RequestEqualsException.class);
    }
}
