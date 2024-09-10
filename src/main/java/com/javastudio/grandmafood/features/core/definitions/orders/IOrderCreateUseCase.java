package com.javastudio.grandmafood.features.core.definitions.orders;

import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;

public interface IOrderCreateUseCase {

    Order create(OrderCreateInput input);
}
