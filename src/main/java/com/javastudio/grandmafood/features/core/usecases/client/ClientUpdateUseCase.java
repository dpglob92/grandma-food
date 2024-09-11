package com.javastudio.grandmafood.features.core.usecases.client;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.client.IClientUpdateUseCase;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientUpdateInput;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import com.javastudio.grandmafood.features.errors.ClientUniqueDocumentException;
import com.javastudio.grandmafood.features.errors.ClientUniqueEmailException;
import com.javastudio.grandmafood.features.errors.RequestEqualsException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class ClientUpdateUseCase implements IClientUpdateUseCase {
    Logger logger = LoggerFactory.getLogger(ClientUpdateUseCase.class);

    private final ClientJPAEntityRepository repository;

    private final ClientFindUseCase findUseCase;

    private final Validator validator;

    private final ClientAdapter clientAdapter;

    public ClientUpdateUseCase(
            ClientJPAEntityRepository repository,
            Validator validator,
            ClientFindUseCase findUseCase,
            ClientAdapter clientAdapter
    ) {
        this.repository = repository;
        this.validator = validator;
        this.findUseCase = findUseCase;
        this.clientAdapter = clientAdapter;
    }

    @Override
    public void updateByDocument(String documentId, ClientUpdateInput input) {
        logger.info("Validating client update");
        ValidationUtils.validate(validator, input);

        logger.info("Finding client with documentId: {}", documentId);
        Optional<Client> clientContainer = findUseCase.findByDocument(documentId);
        if (clientContainer.isEmpty()) {
            throw new ClientNotFoundException();
        }

        ClientJPAEntity clientJPAEntity = clientAdapter.domainToJPAEntity(clientContainer.get());
        if (isClientUnchanged(clientJPAEntity, input)) {
            throw new RequestEqualsException();
        }

        clientJPAEntity.setName(input.getName());
        clientJPAEntity.setEmail(input.getEmail());
        clientJPAEntity.setPhone(input.getPhone());
        clientJPAEntity.setDeliveryAddress(input.getDeliveryAddress());
        clientJPAEntity.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

        try {
            logger.info("Client updated with documentId: {}", clientJPAEntity.getId());
            repository.save(clientJPAEntity);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("client.email_unique_constraint")) {
                throw new ClientUniqueEmailException();
            } else if (e.getMessage().contains("client.document_id_unique_constraint")) {
                throw new ClientUniqueDocumentException();
            }
            throw e;
        }
    }

    private boolean isClientUnchanged(ClientJPAEntity clientJPA, ClientUpdateInput clientUpdateInput) {
        return clientJPA.getDocumentId().equalsIgnoreCase(clientUpdateInput.getDocumentId()) &&
                clientJPA.getName().equals(clientUpdateInput.getName()) &&
                clientJPA.getEmail().equals(clientUpdateInput.getEmail()) &&
                clientJPA.getPhone().compareTo(clientUpdateInput.getPhone()) == 0 &&
                clientJPA.getDeliveryAddress().equals(clientUpdateInput.getDeliveryAddress());
    }
}
