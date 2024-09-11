package com.javastudio.grandmafood.core.unit.order;

import com.javastudio.grandmafood.core.utils.OrderTestUtil;
import com.javastudio.grandmafood.core.utils.ProductTestUtil;
import com.javastudio.grandmafood.features.core.entities.orders.OrderInfo;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItem;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderInfoUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

public class OrderInfoUseCaseTest {

    private final OrderInfoUseCase orderInfoUseCase;

    public OrderInfoUseCaseTest() {
        this.orderInfoUseCase = new OrderInfoUseCase();
    }

    // this is not possible due to the validations perform on order, but we still want a default behavior
    @Test
    public void Should_ReturnZeroForAllValues_WhenListIsZero(){
        var order = OrderTestUtil.getValidOrder();
        order.setOrderItems(List.of());

        OrderInfo orderInfo = orderInfoUseCase.computeOrderInfo(order);

        Assertions.assertThat(orderInfo.getSubTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        Assertions.assertThat(orderInfo.getTax()).isEqualByComparingTo(BigDecimal.ZERO);
        Assertions.assertThat(orderInfo.getGrandTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void Should_ComputeOrderInfo_WhenValuesAreIntegers(){
        var order = OrderTestUtil.getValidOrder();
        var p1 = ProductTestUtil.getValidProduct().toBuilder().price(new BigDecimal("10")).build();
        var p2 = ProductTestUtil.getValidProduct().toBuilder().price(new BigDecimal("15")).build();

        order.setOrderItems(List.of(new OrderItem(p1, 3), new OrderItem(p2, 1)));

        OrderInfo orderInfo = orderInfoUseCase.computeOrderInfo(order);

        Assertions.assertThat(orderInfo.getSubTotal()).isEqualByComparingTo(new BigDecimal("45"));
        Assertions.assertThat(orderInfo.getTax()).isEqualByComparingTo(new BigDecimal("8.55"));
        Assertions.assertThat(orderInfo.getGrandTotal()).isEqualByComparingTo(new BigDecimal("53.55"));
    }

    @Test
    public void Should_ComputeOrderInfo_WhenValuesAreDecimals(){
        var order = OrderTestUtil.getValidOrder();
        var p1 = ProductTestUtil.getValidProduct().toBuilder().price(new BigDecimal("52.64")).build();
        var p2 = ProductTestUtil.getValidProduct().toBuilder().price(new BigDecimal("60.87")).build();

        order.setOrderItems(List.of(new OrderItem(p1, 2), new OrderItem(p2, 3)));

        OrderInfo orderInfo = orderInfoUseCase.computeOrderInfo(order);

        Assertions.assertThat(orderInfo.getSubTotal()).isEqualByComparingTo(new BigDecimal("287.89"));
        Assertions.assertThat(orderInfo.getTax()).isEqualByComparingTo(new BigDecimal("54.6991"));
        Assertions.assertThat(orderInfo.getGrandTotal()).isEqualByComparingTo(new BigDecimal("342.5891"));
    }

    // TODO: create a test for testing if an order did not fetch its order items, in other words, order items is null
}
