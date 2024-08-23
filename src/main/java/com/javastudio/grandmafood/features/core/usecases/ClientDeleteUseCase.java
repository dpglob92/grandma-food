package com.javastudio.grandmafood.features.core.usecases;

import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.client.IClientDeleteUseCase;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class ClientDeleteUseCase implements IClientDeleteUseCase {

    private final ClientJPAEntityRepository repository;

    private final Validator validator;

    public ClientDeleteUseCase(ClientJPAEntityRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void deleteByDocument(String documentNumber) {

    }
}
