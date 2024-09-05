package com.javastudio.grandmafood.features.core.entities.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

/**
 * Represents the information related to an order, including sub-total, tax, and grand total.
 */
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderInfo {

    /**
     * The sub-total amount of the order.
     * it is computed by the following formula: sum(Pi * Qi), where P is the product, Q the quantity
     */
    @NonNull
    private BigDecimal subTotal;

    /**
     * The tax amount applied to the order.
     * it is computed by the following formula: subtotal * IVA, example 123.41 * 0.17
     */
    @NonNull
    private BigDecimal tax;

    /**
     * The grand total amount of the order (including tax).
     * it is computed by the following formula: subTotal + tax
     */
    @NonNull
    private BigDecimal grandTotal;
}
