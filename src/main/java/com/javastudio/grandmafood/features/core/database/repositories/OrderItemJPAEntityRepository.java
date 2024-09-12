package com.javastudio.grandmafood.features.core.database.repositories;

import com.javastudio.grandmafood.features.core.database.entities.OrderItemJPAEntity;
import com.javastudio.grandmafood.features.core.database.projections.OrderItemView;
import com.javastudio.grandmafood.features.core.database.projections.ProductReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderItemJPAEntityRepository extends JpaRepository<OrderItemJPAEntity, Long> {

    @Query("SELECT oi.product as product, oi.quantity as quantity FROM RestaurantOrderItem oi WHERE oi.order.id = ?1")
    List<OrderItemView> findOrderItems(UUID uuid);

    @Query("""
           SELECT
            oi.product.name AS productName,
            oi.product.id AS productId,
            SUM(oi.quantity) AS unitsSold,
            SUM(oi.product.price * oi.quantity) AS grossProfit
           FROM RestaurantOrderItem oi
           WHERE oi.order.createdAt BETWEEN :startDate AND :endDate
           GROUP BY oi.product.id
           ORDER BY unitsSold DESC
           """)
    List<ProductReportView> getSaleReport(LocalDateTime startDate, LocalDateTime endDate);
}