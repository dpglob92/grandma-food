package com.javastudio.grandmafood.core.utils;

import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItem;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItemCreateInput;
import com.javastudio.grandmafood.features.core.entities.product.Product;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

public class OrderTestUtil {

    public static Order getValidOrder() {

        Product product1 = ProductTestUtil.getValidProduct();
        Client client = ClientTestUtil.getValidClient();

        return Order.builder()
                .uuid(UUID.randomUUID())
                .additionalInformation("")
                .orderItems(List.of(
                        new OrderItem(product1, 2)
                ))
                .client(client)
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build();
    }

    public static OrderCreateInput getValidOrderCreateInput() {

        return OrderCreateInput.builder()
                .additionalInformation("")
                .orderItems(List.of(
                        new OrderItemCreateInput(UUID.randomUUID(), 2)
                ))
                .clientDocumentId("CC-100200300")
                .build();
    }
}
