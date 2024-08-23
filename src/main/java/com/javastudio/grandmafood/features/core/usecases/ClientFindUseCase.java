package com.javastudio.grandmafood.features.core.usecases;

import com.javastudio.grandmafood.features.core.definitions.client.IClientFindUseCase;
import com.javastudio.grandmafood.features.core.entities.client.Client;

import java.util.Optional;
import java.util.UUID;

public class ClientFindUseCase implements IClientFindUseCase {
    @Override
    public Optional<Client> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByDocument(String documentId) {
        return Optional.empty();
    }
}
