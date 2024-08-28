package com.javastudio.grandmafood.core.unit.client;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.usecases.client.ClientDeleteUseCase;
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
public class ClientDeleteUseCaseTest {

    @Mock
    private ClientJPAEntityRepository repository;

    private ClientDeleteUseCase clientDeleteUseCase;

    @BeforeEach
    public void setup() {
        clientDeleteUseCase = new ClientDeleteUseCase(repository, Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Test
    public void Should_ThrowNotFoundException_WhenClientIsNotFound() {
        String validDocumentId = "1022010061";
        when(repository.findByDocumentId(validDocumentId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> clientDeleteUseCase.deleteByDocument(validDocumentId))
                .isInstanceOf(ClientNotFoundException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }
}
