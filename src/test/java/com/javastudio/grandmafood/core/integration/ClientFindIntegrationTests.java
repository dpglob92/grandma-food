package com.javastudio.grandmafood.core.integration;

import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.usecases.client.ClientCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientFindIntegrationTests {

    @Autowired
    private ClientCreateUseCase clientCreateUseCase;

    @Autowired
    private ClientFindUseCase clientFindUseCase;

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
    public void Should_FindClient_GivenId() {

        var clientFound = clientFindUseCase.findById(client1.getUuid());

        Assertions.assertThat(clientFound).isNotEmpty();
        Assertions.assertThat(clientFound.get().getUuid()).isEqualTo(client1.getUuid());
    }

    @Test
    public void Should_NotFindClient_GivenId() {

        var clientFound = clientFindUseCase.findById(UUID.randomUUID());

        Assertions.assertThat(clientFound).isEmpty();
    }

    @Test
    public void Should_FindClient_GivenDocumentId() {

        var clientFound = clientFindUseCase.findByDocument(client2.getDocumentId());

        Assertions.assertThat(clientFound).isNotEmpty();
        Assertions.assertThat(clientFound.get().getDocumentId()).isEqualTo(client2.getDocumentId());
    }

    @Test
    public void Should_NotFindClient_GivenDocumentId() {

        var clientFound = clientFindUseCase.findByDocument("1325464684");

        Assertions.assertThat(clientFound).isEmpty();
    }
}
