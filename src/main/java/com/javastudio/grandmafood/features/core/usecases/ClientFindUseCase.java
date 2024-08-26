package com.javastudio.grandmafood.features.core.usecases;

import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.client.IClientFindUseCase;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ClientFindUseCase implements IClientFindUseCase {
    Logger logger = LoggerFactory.getLogger(ClientFindUseCase.class);

    private final ClientJPAEntityRepository repository;
    private final Validator validator;

    public ClientFindUseCase(ClientJPAEntityRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Optional<Client> findById(UUID uuid) {
        validator.validate(uuid);

        logger.info("Finding client with id: {}", uuid);
        Optional<ClientJPAEntity> clientOptional = repository.findById(uuid);
        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException();
        }
        return clientOptional.map(ClientAdapter::jpaEntityToDomain);
    }

    @Override
    public Optional<Client> findByDocument(String documentId) {
        validator.validate(documentId);

        logger.info("Finding client with documentId: {}", documentId);
        Optional<ClientJPAEntity> clientOptional = repository.findByDocumentId(documentId);
        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException();
        }
        return clientOptional.map(ClientAdapter::jpaEntityToDomain);
    }
}
