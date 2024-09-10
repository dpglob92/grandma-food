package com.javastudio.grandmafood.core.integration.order;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.features.core.database.entities.OrderJPAEntity;
import com.javastudio.grandmafood.features.core.database.projections.OrderItemView;
import com.javastudio.grandmafood.features.core.database.repositories.OrderItemJPAEntityRepository;
import com.javastudio.grandmafood.features.core.database.repositories.OrderJPAEntityRepository;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItemCreateInput;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderCreateUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import com.javastudio.grandmafood.features.errors.ProductNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderCreateIntegrationTests {

    @Autowired
    private OrderCreateUseCase createUseCase;

    @Autowired
    private OrderItemJPAEntityRepository orderItemJPAEntityRepository;

    @Autowired
    private OrderJPAEntityRepository orderJPAEntityRepository;

    private final UUID client1Id = UUID.fromString("3f06af63-a93c-11e4-9797-00505690773f");
    private final String client1DocId = "100300200";

    private final UUID product1Id = UUID.fromString("5421030d-04db-46ab-b5f2-2429c6b2d0d9");

    @Test
    @Sql({"classpath:setup-data/dummy-client.sql", "classpath:setup-data/dummy-product.sql"})
    public void Should_CreateAndPersistOrder_WhenDataIsValid() {

        var validInput = OrderCreateInput.builder()
                .additionalInformation("hot food")
                .orderItems(List.of(
                        new OrderItemCreateInput(product1Id, 2)
                ))
                .clientDocumentId(client1DocId)
                .build();


        Order order = createUseCase.create(validInput);
        Optional<OrderJPAEntity> foundOrderContainer = orderJPAEntityRepository.findById(order.getUuid());
        List<OrderItemView> foundOrderItems = orderItemJPAEntityRepository.findOrderItems(order.getUuid());

        // When using order create use-case, order items cannot be null
        Assertions.assertThat(order.getOrderItems()).hasSize(1);

        // Asserting persistence of order
        if (foundOrderContainer.isPresent()) {
            OrderJPAEntity foundOrder = foundOrderContainer.get();
            Assertions.assertThat(foundOrder.getAdditionalInformation()).isEqualTo("hot food");
            Assertions.assertThat(foundOrder.getClient().getId()).isEqualTo(client1Id);
            Assertions.assertThat(foundOrder.getClient().getDocumentId()).isEqualTo(client1DocId);
            Assertions.assertThat(foundOrder.getDeliveredAt()).isNull();
        } else {
            Assertions.fail("order was not found");
        }

        // Asserting persistence of order item
        Assertions.assertThat(foundOrderItems).hasSize(1);
        Assertions.assertThat(foundOrderItems.getFirst().getProduct().getId()).isEqualTo(product1Id);
        Assertions.assertThat(foundOrderItems.getFirst().getQuantity()).isEqualTo(2);
    }

    @Test
    public void Should_ThrowClientNotFoundException_WhenClientIsNotFound() {
        var validInput = OrderCreateInput.builder()
                .additionalInformation("hot food")
                .orderItems(List.of(
                        new OrderItemCreateInput(product1Id, 2)
                ))
                .clientDocumentId("200300511")
                .build();

        Assertions.assertThatThrownBy(() -> createUseCase.create(validInput))
                .isInstanceOf(ClientNotFoundException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }

    @Test
    @Sql({"classpath:setup-data/dummy-client.sql"})
    public void Should_ThrowProductNotFoundException_WhenOneOfTheProductsInOrderItemsIsNotFound() {

        var validInput = OrderCreateInput.builder()
                .additionalInformation("hot food")
                .orderItems(List.of(
                        new OrderItemCreateInput(product1Id, 2)
                ))
                .clientDocumentId(client1DocId)
                .build();

        var ex = Assertions.assertThatThrownBy(() -> createUseCase.create(validInput));
        ex.isInstanceOf(ProductNotFoundException.class);
        ex.extracting("exceptionCode").isEqualTo(ExceptionCode.NOT_FOUND);
        ex.hasMessageContaining(product1Id.toString());
    }
}
