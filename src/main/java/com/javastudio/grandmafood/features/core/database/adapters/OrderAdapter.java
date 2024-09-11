package com.javastudio.grandmafood.features.core.database.adapters;

import com.javastudio.grandmafood.features.core.database.entities.OrderJPAEntity;
import com.javastudio.grandmafood.features.core.database.projections.OrderItemView;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderAdapter {

    private final ClientAdapter clientAdapter;

    private final ProductAdapter productAdapter;

    public OrderAdapter(ClientAdapter clientAdapter, ProductAdapter productAdapter) {
        this.clientAdapter = clientAdapter;
        this.productAdapter = productAdapter;
    }

    public Order jpaEntityToDomain(OrderJPAEntity orderJPAEntity) {
        return Order.builder()
                .uuid(orderJPAEntity.getId())
                .client(clientAdapter.jpaEntityToDomain(orderJPAEntity.getClient()))
                .additionalInformation(orderJPAEntity.getAdditionalInformation())
                .deliveredAt(orderJPAEntity.getDeliveredAt())
                .createdAt(orderJPAEntity.getCreatedAt())
                .updatedAt(orderJPAEntity.getUpdatedAt())
                .build();
    }

    public Order jpaEntityToDomain(OrderJPAEntity orderJPAEntity, List<OrderItemView> orderItemJPAEntities) {
        Order order = this.jpaEntityToDomain(orderJPAEntity);
        List<OrderItem> orderItems = orderItemJPAEntities.stream().map(
                oiJpa -> new OrderItem(productAdapter.jpaEntityToDomain(oiJpa.getProduct()), oiJpa.getQuantity())
        ).toList();

        order.setOrderItems(orderItems);

        return order;
    }
}
