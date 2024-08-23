package com.javastudio.grandmafood.core.integration;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.usecases.ClientCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.ClientDeleteUseCase;
import com.javastudio.grandmafood.features.core.usecases.ClientFindUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // clean db for each test
public class ClientDeleteIntegrationTests {

    @Autowired
    private ClientDeleteUseCase clientDeleteUseCase;

    @Autowired
    private ClientCreateUseCase clientCreateUseCase;

    @Autowired
    private ClientFindUseCase clientFindUseCase;

    private Client client1;

    @BeforeEach
    public void setup() {
        var validInput1 = ClientTestUtil.getValidClientCreateInput();
        this.client1 = clientCreateUseCase.create(validInput1);

        var validInput2 = ClientTestUtil.getValidClientCreateInput().toBuilder()
                .documentId("1005004001")
                .name("Jul Kon")
                .email("julkon@ema.com")
                .build();
        clientCreateUseCase.create(validInput2);
    }

    @Test
    public void Should_DeleteClient_GivenDocumentId() {

        clientDeleteUseCase.deleteByDocument(client1.getDocumentId());

        var clientFound = clientFindUseCase.findByDocument(client1.getDocumentId());

        Assertions.assertThat(clientFound).isEmpty();
    }

    @Test
    public void Should_ThrowNotFoundException_WhenClientIsNotFound() {

        Assertions.assertThatThrownBy(() -> clientDeleteUseCase.deleteByDocument("1003001004"))
                .isInstanceOf(ClientNotFoundException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }
}
