package com.javastudio.grandmafood.features.core.database.repositories;

import com.javastudio.grandmafood.features.core.database.entities.OrderItemJPAEntity;
import com.javastudio.grandmafood.features.core.database.projections.OrderItemView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderItemJPAEntityRepository extends JpaRepository<OrderItemJPAEntity, Long> {

    @Query("SELECT oi.product as product, oi.quantity as quantity FROM RestaurantOrderItem oi WHERE oi.order.id = ?1")
    List<OrderItemView> findOrderItems(UUID uuid);
}