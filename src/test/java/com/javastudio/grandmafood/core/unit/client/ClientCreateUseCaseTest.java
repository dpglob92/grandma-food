package com.javastudio.grandmafood.core.unit.client;

import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import com.javastudio.grandmafood.features.core.usecases.ClientCreateUseCase;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientCreateUseCaseTest {

    @Mock
    private ClientJPAEntityRepository repository;

    private ClientCreateUseCase clientCreateUseCase;

    @BeforeEach
    public void setup() {
        clientCreateUseCase = new ClientCreateUseCase(repository, Validation.buildDefaultValidatorFactory().getValidator());
    }

    private void commonInvalidInputTest(Function<ClientCreateInput, ClientCreateInput> fieldSetter, String fieldName) {
        var validInput = ClientTestUtil.getValidClientCreateInput();
        var finalInput = fieldSetter.apply(validInput);
        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> clientCreateUseCase.create(finalInput),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.getFirst().path()).isEqualTo(fieldName);
    }

    @Test
    public void Should_CreateClient_WhenDataIsValid() {
        var validInput = ClientTestUtil.getValidClientCreateInput();

        clientCreateUseCase.create(validInput);

        verify(repository).save(Mockito.any(ClientJPAEntity.class));
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenDocumentIdIsTooLong() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().documentId("a".repeat(16)).build(),
                "documentId"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenDocumentIdIsEmpty() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().documentId("").build(),
                "documentId"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenNameIsTooLong() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().name("a".repeat(256)).build(),
                "name"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenNameIsEmpty() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().name("").build(),
                "name"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenEmailIsEmpty() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().email("").build(),
                "email"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"juan", "pepe@c.", "@aaa.com"})
    public void Should_ThrowInvalidInputException_WhenEmailIsInvalid(String input) {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().email(input).build(),
                "email"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenPhoneIsEmpty() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().phone("").build(),
                "phone"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenPhoneIsTooLong() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().phone("3a2-88890901").build(),
                "phone"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"322-1010", "3228889090", "3a2-8889090"})
    public void Should_ThrowInvalidInputException_WhenPhoneIsInvalid() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().phone("").build(),
                "phone"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenDeliveryAddressIsEmpty() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().deliveryAddress("").build(),
                "deliveryAddress"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenDeliveryAddressIsTooLong() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().deliveryAddress("a".repeat(501)).build(),
                "deliveryAddress"
        );
    }
}
