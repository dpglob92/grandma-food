package com.javastudio.grandmafood.features.core.usecases.client;

import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.client.IClientFindUseCase;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ClientFindUseCase implements IClientFindUseCase {
    Logger logger = LoggerFactory.getLogger(ClientFindUseCase.class);

    private final ClientJPAEntityRepository repository;

    private final ClientAdapter clientAdapter;

    public ClientFindUseCase(ClientJPAEntityRepository repository, ClientAdapter clientAdapter) {
        this.repository = repository;
        this.clientAdapter = clientAdapter;
    }

    @Override
    public Optional<Client> findById(UUID uuid) {
        logger.info("Finding client with id: {}", uuid);
        Optional<ClientJPAEntity> clientOptional = repository.findById(uuid);
        return clientOptional.map(clientAdapter::jpaEntityToDomain);
    }

    @Override
    public Optional<Client> findByDocument(String documentId) {
        logger.info("Finding client with documentId: {}", documentId);
        Optional<ClientJPAEntity> clientOptional = repository.findByDocumentId(documentId);
        return clientOptional.map(clientAdapter::jpaEntityToDomain);
    }

    @Override
    public List<Client> getClientsSorted(String orderBy, String direction) {
        if (!List.of("DOCUMENT", "NAME", "ADDRESS").contains(orderBy)) {
            orderBy = "DOCUMENT";
        }

        Sort.Direction sortDirection = direction.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        String sortBy = switch (orderBy) {
            case "NAME" -> "name";
            case "ADDRESS" -> "deliveryAddress";
            default -> "documentId";
        };

        Sort sort = Sort.by(sortDirection, sortBy);

        List<ClientJPAEntity> clientEntities = repository.findAll(sort);
        return clientEntities.stream()
                .map(clientAdapter::jpaEntityToDomain)
                .collect(Collectors.toList());
    }
}
