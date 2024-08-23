package com.javastudio.grandmafood.features.core.definitions.client;

import com.javastudio.grandmafood.features.core.entities.client.Client;

import java.util.Optional;
import java.util.UUID;

public interface IClientFindUseCase {

    Optional<Client> findById(UUID uuid);

    Optional<Client> findByDocument(String documentId);
}
