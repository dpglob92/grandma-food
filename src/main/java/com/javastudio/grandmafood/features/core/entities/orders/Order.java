package com.javastudio.grandmafood.features.core.entities.orders;

import com.javastudio.grandmafood.features.core.entities.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents an order placed by a client.
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Order {

    /**
     * The unique identifier for the order.
     */
    @NonNull
    private UUID uuid;

    /**
     * The client who placed the order.
     */
    @NonNull
    private Client client;

    /**
     * The list of items included in the order. if null the order items were not fetched
     */
    private List<OrderItem> orderItems;

    /**
     * Additional information or notes about the order. Example: Hamburger without tomato sauce
     */
    @NonNull
    private String additionalInformation;

    /**
     * The date and time when the order was delivered. if null, the order has not been delivered
     */
    private LocalDateTime deliveredAt;

    /**
     * The date and time when the order was created.
     */
    @NonNull
    private LocalDateTime createdAt;

    /**
     * The date and time when the order was last updated.
     */
    @NonNull
    private LocalDateTime updatedAt;

    /**
     * The date and time when the order was deleted, if applicable.
     */
    private LocalDateTime deletedAt;
}
