package com.javastudio.grandmafood.features.core.database.repositories;

import com.javastudio.grandmafood.features.core.database.entities.OrderItemJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemJPAEntityRepository extends JpaRepository<OrderItemJPAEntity, Long> {
}