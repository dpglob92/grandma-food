package com.javastudio.grandmafood.core.unit.order;

import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.core.utils.OrderTestUtil;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItemCreateInput;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductFindUseCase;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class OrderCreateUseCaseTest {

    @Mock
    private ProductFindUseCase productFindUseCase;

    @Mock
    private ClientFindUseCase clientFindUseCase;

    private OrderCreateUseCase createUseCase;

    @BeforeEach
    public void setup() {
        createUseCase = new OrderCreateUseCase(
                productFindUseCase,
                clientFindUseCase,
                Validation.buildDefaultValidatorFactory().getValidator()
        );
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
                createInput -> createInput.toBuilder().clientId(null).build(),
                "clientId"
        );
    }
}
