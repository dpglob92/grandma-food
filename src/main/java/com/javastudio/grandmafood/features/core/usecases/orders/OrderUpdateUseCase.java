package com.javastudio.grandmafood.features.core.usecases.orders;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.database.adapters.OrderAdapter;
import com.javastudio.grandmafood.features.core.database.entities.OrderJPAEntity;
import com.javastudio.grandmafood.features.core.database.projections.OrderItemView;
import com.javastudio.grandmafood.features.core.database.repositories.OrderItemJPAEntityRepository;
import com.javastudio.grandmafood.features.core.database.repositories.OrderJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.orders.IOrderUpdateUseCase;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.errors.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderUpdateUseCase implements IOrderUpdateUseCase {

    private final OrderJPAEntityRepository repository;

    private final OrderItemJPAEntityRepository orderItemJPAEntityRepository;

    private final OrderAdapter orderAdapter;

    Logger logger = LoggerFactory.getLogger(OrderUpdateUseCase.class);

    public OrderUpdateUseCase(
            OrderJPAEntityRepository repository,
            OrderItemJPAEntityRepository orderItemJPAEntityRepository, OrderAdapter orderAdapter
            ) {
        this.repository = repository;
        this.orderItemJPAEntityRepository = orderItemJPAEntityRepository;
        this.orderAdapter = orderAdapter;
    }


    @Override
    public Order updateDeliveryDate(UUID uuid, String timestamp) {
        logger.info("Validating and parsing delivery date timestamp");
        LocalDateTime parsedDate = ValidationUtils.parseToLocalDateTime(timestamp, "deliveryDate");

        Optional<OrderJPAEntity> entityContainer = repository.findById(uuid);

        if (entityContainer.isEmpty()) {
            throw new OrderNotFoundException();
        }

        logger.info("updating delivery date to timestamp {} of order with id: {}", timestamp, uuid);
        OrderJPAEntity orderJPAEntity = entityContainer.get();
        orderJPAEntity.setDeliveredAt(parsedDate);
        orderJPAEntity.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        OrderJPAEntity saved = this.repository.save(orderJPAEntity);

        logger.info("fetching order items of order with id: {}", uuid);
        List<OrderItemView> foundOrderItems = orderItemJPAEntityRepository.findOrderItems(uuid);

        return orderAdapter.jpaEntityToDomain(saved, foundOrderItems);
    }
}
