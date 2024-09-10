package com.javastudio.grandmafood.core.unit.order;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.core.utils.OrderTestUtil;
import com.javastudio.grandmafood.core.utils.ProductTestUtil;
import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.adapters.ProductAdapter;
import com.javastudio.grandmafood.features.core.database.entities.OrderJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.OrderItemJPAEntityRepository;
import com.javastudio.grandmafood.features.core.database.repositories.OrderJPAEntityRepository;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItemCreateInput;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductFindUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import com.javastudio.grandmafood.features.errors.ProductNotFoundException;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderCreateUseCaseTest {

    @Mock
    private ProductFindUseCase productFindUseCase;

    @Mock
    private ClientFindUseCase clientFindUseCase;

    @Mock
    private OrderItemJPAEntityRepository orderItemJPAEntityRepository;

    @Mock
    private OrderJPAEntityRepository orderJPAEntityRepository;

    @Mock
    private ClientAdapter clientAdapter;

    @Mock
    private ProductAdapter productAdapter;

    private OrderCreateUseCase createUseCase;

    @BeforeEach
    public void setup() {
        createUseCase = new OrderCreateUseCase(
                productFindUseCase,
                clientFindUseCase,
                orderJPAEntityRepository,
                orderItemJPAEntityRepository,
                clientAdapter,
                productAdapter,
                Validation.buildDefaultValidatorFactory().getValidator()
        );
    }

    @Test
    public void Should_ThrowClientNotFoundException_WhenClientIsNotFound() {
        var validInput = OrderTestUtil.getValidOrderCreateInput();

        when(clientFindUseCase.findByDocument(Mockito.any(String.class))).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> createUseCase.create(validInput))
                .isInstanceOf(ClientNotFoundException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }

    @Test
    public void Should_ThrowProductNotFoundException_WhenOneOfTheProductsInOrderItemsIsNotFound() {
        // Arrange

        var productId1 = UUID.randomUUID();
        var productId2 = UUID.randomUUID();

        var validInput = OrderCreateInput.builder()
                .additionalInformation("")
                .orderItems(List.of(
                        new OrderItemCreateInput(productId1, 2),
                        new OrderItemCreateInput(productId2, 4)
                ))
                .clientDocumentId("CC-100200300")
                .build();

        when(clientFindUseCase.findByDocument(Mockito.any(String.class)))
                .thenReturn(Optional.of(ClientTestUtil.getValidClient()));

        var validProduct = ProductTestUtil.getValidProduct().toBuilder().uuid(productId1).build();

        when(productFindUseCase.findById(Mockito.eq(productId1)))
                .thenReturn(Optional.of(validProduct));
        when(productFindUseCase.findById(Mockito.eq(productId2)))
                .thenReturn(Optional.empty());

        // Act and assert

        var ex = Assertions.assertThatThrownBy(() -> createUseCase.create(validInput));
        ex.isInstanceOf(ProductNotFoundException.class);
        ex.extracting("exceptionCode").isEqualTo(ExceptionCode.NOT_FOUND);
        ex.hasMessageContaining(productId2.toString());

    }

    @Test
    public void Should_SaveOrderWithTheCorrectOrderItems_WhenDataIsValid() {
        var productId1 = UUID.randomUUID();
        var productId2 = UUID.randomUUID();

        var validInput = OrderCreateInput.builder()
                .additionalInformation("")
                .orderItems(List.of(
                        new OrderItemCreateInput(productId1, 2),
                        new OrderItemCreateInput(productId2, 4)
                ))
                .clientDocumentId("CC-100200300")
                .build();

        when(clientFindUseCase.findByDocument(Mockito.any(String.class)))
                .thenReturn(Optional.of(ClientTestUtil.getValidClient()));

        var validProduct1 = ProductTestUtil.getValidProduct().toBuilder().uuid(productId1).build();
        var validProduct2 = ProductTestUtil.getValidProduct().toBuilder().uuid(productId2).build();

        when(productFindUseCase.findById(Mockito.eq(productId1)))
                .thenReturn(Optional.of(validProduct1));
        when(productFindUseCase.findById(Mockito.eq(productId2)))
                .thenReturn(Optional.of(validProduct2));

        // we are only mocking this so it does not throw an error when building the order
        when(orderJPAEntityRepository.save(Mockito.any())).thenReturn(
                OrderJPAEntity.builder()
                        .id(UUID.randomUUID())
                        .additionalInformation("")
                        .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                        .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                        .build()
        );

        var order = createUseCase.create(validInput);

        Assertions.assertThat(order.getOrderItems()).hasSize(2);
        var orderItem1 = order.getOrderItems().getFirst();
        var orderItem2 = order.getOrderItems().getLast();

        Assertions.assertThat(orderItem1.getProduct().getUuid()).isEqualTo(productId1);
        Assertions.assertThat(orderItem1.getQuantity()).isEqualTo(2);

        Assertions.assertThat(orderItem2.getProduct().getUuid()).isEqualTo(productId2);
        Assertions.assertThat(orderItem2.getQuantity()).isEqualTo(4);
    }


    private void commonInvalidInputTest(Function<OrderCreateInput, OrderCreateInput> fieldSetter, String fieldName) {
        var validInput = OrderTestUtil.getValidOrderCreateInput();
        var finalInput = fieldSetter.apply(validInput);
        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> createUseCase.create(finalInput),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        System.out.println(errors.stream().map(FieldError::path).toList());
        System.out.println(errors.getFirst().path());
        Assertions.assertThat(errors.getFirst().path()).isEqualTo(fieldName);
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenAdditionalInformationIsNull() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().additionalInformation(null).build(),
                "additionalInformation"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenAdditionalInformationIsTooLong() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().additionalInformation("a".repeat(512)).build(),
                "additionalInformation"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenOrderItemsIsNull() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().orderItems(null).build(),
                "orderItems"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenOrderItemsAreEmpty() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().orderItems(List.of()).build(),
                "orderItems"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenOrderItemsThresholdIsReach() {

        List<OrderItemCreateInput> orderItems = Stream.generate(() -> new OrderItemCreateInput(UUID.randomUUID(), 2))
                .limit(151)
                .toList();

        commonInvalidInputTest(
                createInput -> createInput.toBuilder().orderItems(orderItems).build(),
                "orderItems"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenProductIdIsNullInOrderItems() {

        List<OrderItemCreateInput> orderItems = List.of(
                new OrderItemCreateInput(UUID.randomUUID(), 2),
                new OrderItemCreateInput(null, 5)
        );

        commonInvalidInputTest(
                createInput -> createInput.toBuilder().orderItems(orderItems).build(),
                "orderItems[1].productId"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenQuantityNullInOrderItems() {

        List<OrderItemCreateInput> orderItems = List.of(
                new OrderItemCreateInput(UUID.randomUUID(), null),
                new OrderItemCreateInput(UUID.randomUUID(), 5)
        );

        commonInvalidInputTest(
                createInput -> createInput.toBuilder().orderItems(orderItems).build(),
                "orderItems[0].quantity"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenQuantityIsZeroInOrderItems() {

        List<OrderItemCreateInput> orderItems = List.of(
                new OrderItemCreateInput(UUID.randomUUID(), 0)
        );

        commonInvalidInputTest(
                createInput -> createInput.toBuilder().orderItems(orderItems).build(),
                "orderItems[0].quantity"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenQuantityIsNegativeInOrderItems() {

        List<OrderItemCreateInput> orderItems = List.of(
                new OrderItemCreateInput(UUID.randomUUID(), -13)
        );

        commonInvalidInputTest(
                createInput -> createInput.toBuilder().orderItems(orderItems).build(),
                "orderItems[0].quantity"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenQuantityIsGreaterThan100InOrderItems() {

        List<OrderItemCreateInput> orderItems = List.of(
                new OrderItemCreateInput(UUID.randomUUID(), 101)
        );

        commonInvalidInputTest(
                createInput -> createInput.toBuilder().orderItems(orderItems).build(),
                "orderItems[0].quantity"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenClientIdIsNull() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().clientDocumentId(null).build(),
                "clientDocumentId"
        );
    }
}
