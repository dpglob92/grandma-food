package com.javastudio.grandmafood.features.core.usecases;

import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.client.IClientCreateUseCase;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import jakarta.validation.Validator;

public class ClientCreateUseCase implements IClientCreateUseCase {

    private final ClientJPAEntityRepository repository;

    private final Validator validator;

    public ClientCreateUseCase(ClientJPAEntityRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Client create(ClientCreateInput input) {
        return null;
    }
}
