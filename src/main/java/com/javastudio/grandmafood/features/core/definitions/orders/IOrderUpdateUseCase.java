package com.javastudio.grandmafood.features.core.definitions.orders;

import com.javastudio.grandmafood.features.core.entities.orders.Order;

import java.util.UUID;

public interface IOrderUpdateUseCase {

    Order updateDeliveryDate(UUID uuid, String timestamp);
}
