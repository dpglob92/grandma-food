package com.javastudio.grandmafood.features.core.database.repositories;

import com.javastudio.grandmafood.features.core.database.entities.ProductJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductJPAEntityRepository extends JpaRepository<ProductJPAEntity, UUID> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.name ASC")
    List<ProductJPAEntity> searchByFantasyName(@Param("name") String name);
}