package com.javastudio.grandmafood.features.core.usecases;

import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.client.IClientDeleteUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class ClientDeleteUseCase implements IClientDeleteUseCase {

    Logger logger = LoggerFactory.getLogger(ClientDeleteUseCase.class);

    private final ClientJPAEntityRepository repository;

    private final Validator validator;

    public ClientDeleteUseCase(ClientJPAEntityRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void deleteByDocument(String documentNumber) {
        validator.validate(documentNumber);

        logger.info("Finding client with documentId: {}", documentNumber);
        Optional<ClientJPAEntity> clientOptional = repository.findByDocumentId(documentNumber);
        if (clientOptional.isEmpty()) {
            throw new ClientNotFoundException();
        }
        clientOptional.get().setDeletedAt(LocalDateTime.now(ZoneOffset.UTC));
        repository.save(clientOptional.get());
        logger.info("Client deleted with documentId: {}", documentNumber);
    }
}
