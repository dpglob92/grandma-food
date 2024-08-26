package com.javastudio.grandmafood.features.core.usecases;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.client.IClientCreateUseCase;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import com.javastudio.grandmafood.features.errors.ClientUniqueDocumentException;
import com.javastudio.grandmafood.features.errors.ClientUniqueEmailException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClientCreateUseCase implements IClientCreateUseCase {
    Logger logger = LoggerFactory.getLogger(ClientCreateUseCase.class);

    private final ClientJPAEntityRepository repository;

    private final Validator validator;

    public ClientCreateUseCase(ClientJPAEntityRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Client create(ClientCreateInput input) {
        // validate inputs, documentId and email
        ValidationUtils.validate(validator, input);

        if (repository.findByDocumentId(input.getDocumentId()).isPresent()) {
            throw new ClientUniqueDocumentException();
        }

        if (repository.findByEmail(input.getEmail()).isPresent()) {
            throw new ClientUniqueEmailException();
        }

        logger.debug("Creating client with name: {} and documentId: {}", input.getName(), input.getDocumentId());
        ClientJPAEntity clientJPA = ClientJPAEntity.builder()
                .documentType(input.getDocumentType())
                .documentId(input.getDocumentId())
                .name(input.getName())
                .email(input.getEmail())
                .phone(input.getPhone())
                .deliveryAddress(input.getDeliveryAddress())
                .build();

        ClientJPAEntity clientCreated = repository.save(clientJPA);
        logger.info("Client created with id: {}", clientCreated.getId());
        return ClientAdapter.jpaEntityToDomain(clientCreated);
    }
}
