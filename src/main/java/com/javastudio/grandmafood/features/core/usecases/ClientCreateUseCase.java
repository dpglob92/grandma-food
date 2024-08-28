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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

        // validate inputs
        ValidationUtils.validate(validator, input);

        logger.debug("Creating client with name: {} and documentId: {}", input.getName(), input.getDocumentId());
        ClientJPAEntity clientJPA = ClientJPAEntity.builder()
                .documentType(input.getDocumentType())
                .documentId(input.getDocumentId())
                .name(input.getName())
                .email(input.getEmail())
                .phone(input.getPhone())
                .deliveryAddress(input.getDeliveryAddress())
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        try {
            ClientJPAEntity clientCreated = repository.save(clientJPA);
            logger.info("Client created with documentId: {}", clientJPA.getId());
            return ClientAdapter.jpaEntityToDomain(clientCreated);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("client.email_unique_constraint")) {
                throw new ClientUniqueEmailException();
            } else if (e.getMessage().contains("client.document_id_unique_constraint")) {
                throw new ClientUniqueDocumentException();
            }
            throw e;
        }


    }
}
