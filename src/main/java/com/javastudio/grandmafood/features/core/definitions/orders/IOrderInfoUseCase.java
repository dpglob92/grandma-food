package com.javastudio.grandmafood.features.core.definitions.orders;

import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderInfo;

public interface IOrderInfoUseCase {

    OrderInfo computeOrderInfo(Order order);
}
