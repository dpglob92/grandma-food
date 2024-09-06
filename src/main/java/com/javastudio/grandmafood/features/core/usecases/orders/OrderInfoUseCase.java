package com.javastudio.grandmafood.features.core.usecases.orders;

import com.javastudio.grandmafood.features.core.definitions.orders.IOrderInfoUseCase;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderInfoUseCase implements IOrderInfoUseCase {

    public static final BigDecimal ivaAsPercentage = new BigDecimal("0.19");

    @Override
    public OrderInfo computeOrderInfo(Order order) {
        return new OrderInfo(
                new BigDecimal("0"),
                new BigDecimal("0"),
                new BigDecimal("0")
        );
    }
}
