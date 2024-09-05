package com.javastudio.grandmafood.features.core.entities.orders;

import com.javastudio.grandmafood.features.core.entities.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Represents an item included in an order, consisting of a product and quantity.
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderItem {

    /**
     * The product included in the order item.
     */
    @NonNull
    private Product product;

    /**
     * The quantity of the product included in the order item.
     */
    @NonNull
    private Integer quantity;
}
