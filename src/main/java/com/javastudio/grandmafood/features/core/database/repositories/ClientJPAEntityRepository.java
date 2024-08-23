package com.javastudio.grandmafood.features.core.database.repositories;

import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientJPAEntityRepository extends JpaRepository<ClientJPAEntity, UUID> {

    Optional<ClientJPAEntity> findByDocumentId(String documentId);
}