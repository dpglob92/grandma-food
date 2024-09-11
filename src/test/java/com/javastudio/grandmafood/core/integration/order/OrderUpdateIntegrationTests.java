package com.javastudio.grandmafood.core.integration.order;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.features.core.database.entities.OrderJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.OrderJPAEntityRepository;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderUpdateUseCase;
import com.javastudio.grandmafood.features.errors.OrderNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderUpdateIntegrationTests {

    @Autowired
    private OrderJPAEntityRepository orderJPAEntityRepository;

    @Autowired
    private OrderUpdateUseCase orderUpdateUseCase;

    private final UUID orderId = UUID.fromString("cbc1892f-fc74-4791-bd62-86d8952abc7c");

    @Test
    @Sql({"classpath:setup-data/dummy-order.sql"})
    public void Should_UpdateDeliveryDateOfOrder_WhenDataIsValid() {

        orderUpdateUseCase.updateDeliveryDate(
                orderId,
                "2022-05-10T21:15:03"
        );

        Optional<OrderJPAEntity> entityContainer = orderJPAEntityRepository.findById(orderId);
        if (entityContainer.isEmpty()) {
            Assertions.fail("order not found");
        } else {
            LocalDateTime deliveredAt = entityContainer.get().getDeliveredAt();
            Assertions.assertThat(deliveredAt.getYear()).isEqualTo(2022);
            Assertions.assertThat(deliveredAt.getMonth()).isEqualTo(Month.MAY);
            Assertions.assertThat(deliveredAt.getDayOfMonth()).isEqualTo(10);
            Assertions.assertThat(deliveredAt.getHour()).isEqualTo(21);
            Assertions.assertThat(deliveredAt.getMinute()).isEqualTo(15);
            Assertions.assertThat(deliveredAt.getSecond()).isEqualTo(3);
        }

    }

    @Test
    @Sql({"classpath:setup-data/dummy-order.sql"})
    public void Should_ThrowOrderNotFoundException_WhenOrderIsNotFound(){

        Assertions.assertThatThrownBy(() -> orderUpdateUseCase.updateDeliveryDate(UUID.randomUUID(), "2022-05-10T21:15:03"))
                .isInstanceOf(OrderNotFoundException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }

}
