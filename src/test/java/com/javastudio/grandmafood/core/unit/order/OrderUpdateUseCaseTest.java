package com.javastudio.grandmafood.core.unit.order;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.features.core.database.adapters.OrderAdapter;
import com.javastudio.grandmafood.features.core.database.repositories.OrderItemJPAEntityRepository;
import com.javastudio.grandmafood.features.core.database.repositories.OrderJPAEntityRepository;
import com.javastudio.grandmafood.features.core.usecases.orders.OrderUpdateUseCase;
import com.javastudio.grandmafood.features.errors.OrderNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderUpdateUseCaseTest {

    @Mock
    private OrderJPAEntityRepository orderJPAEntityRepository;

    @Mock
    private OrderItemJPAEntityRepository orderItemJPAEntityRepository;

    @Mock
    private OrderAdapter orderAdapter;

    private OrderUpdateUseCase updateUseCase;

    @BeforeEach
    public void setup() {
        updateUseCase = new OrderUpdateUseCase(orderJPAEntityRepository, orderItemJPAEntityRepository, orderAdapter);
    }

    private void assertInvalidDateInput(ThrowableAssert.ThrowingCallable callMethod){
        InvalidInputException throwable = Assertions.catchThrowableOfType(
                callMethod,
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.getFirst().path()).isEqualTo("deliveryDate");
    }

    @Test
    public void Should_ThrowOrderNotFoundException_WhenOrderIsNotFound() {
        var randomUuid = UUID.randomUUID();

        when(orderJPAEntityRepository.findById(randomUuid)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> updateUseCase.updateDeliveryDate(randomUuid, "2022-10-10T13:15:00"))
                .isInstanceOf(OrderNotFoundException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenDeliveryDateIsNull() {
        assertInvalidDateInput(
                () -> updateUseCase.updateDeliveryDate(UUID.randomUUID(), null)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "hi",
            "x-02-12T19:15:00",
            "10-50-66T19:15:00",
            "2022-10-10T29:15:00",
    })
    public void Should_ThrowInvalidInputException_WhenDeliveryDateIsInvalid(String input) {
        assertInvalidDateInput(
                () -> updateUseCase.updateDeliveryDate(UUID.randomUUID(), input)
        );
    }
}