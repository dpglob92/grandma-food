package com.javastudio.grandmafood.core.unit.client;

import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.entities.client.ClientUpdateInput;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import com.javastudio.grandmafood.features.core.usecases.client.ClientUpdateUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientUpdateUseCaseTest {

    @Mock
    private ClientJPAEntityRepository repository;

    @Mock
    private ClientUpdateUseCase updateUseCase;

    @Mock
    private ClientFindUseCase findUseCase;

    @Mock
    private ClientAdapter clientAdapter;

    @BeforeEach
    public void setup() {
        updateUseCase = new ClientUpdateUseCase(repository, Validation.buildDefaultValidatorFactory().getValidator(), findUseCase, clientAdapter);
    }

    @Test
    public void Should_ThrowClientNotFoundException_WhenClientIsNotFound() {

        String documentId = "123456";
        ClientUpdateInput validInput = ClientTestUtil.getValidClientUpdateInput();

        when(findUseCase.findByDocument(documentId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> updateUseCase.updateByDocument(documentId, validInput))
                .isInstanceOf(ClientNotFoundException.class);
    }

}
