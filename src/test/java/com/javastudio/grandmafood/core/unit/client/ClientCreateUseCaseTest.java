package com.javastudio.grandmafood.core.unit.client;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.core.utils.ClientTestUtil;
import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ClientJPAEntityRepository;
import com.javastudio.grandmafood.features.core.entities.client.ClientCreateInput;
import com.javastudio.grandmafood.features.core.usecases.client.ClientCreateUseCase;
import com.javastudio.grandmafood.features.errors.ClientUniqueDocumentException;
import com.javastudio.grandmafood.features.errors.ClientUniqueEmailException;
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
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ClientCreateUseCaseTest {

    @Mock
    private ClientJPAEntityRepository repository;

    @Mock
    private ClientAdapter clientAdapter;

    private ClientCreateUseCase clientCreateUseCase;

    @BeforeEach
    public void setup() {
        clientCreateUseCase = new ClientCreateUseCase(
                repository,
                Validation.buildDefaultValidatorFactory().getValidator(),
                clientAdapter
        );
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
    public void Should_ThrowUniqueDocumentException_WhenDocumentIsDuplicated() {
        var validInput = ClientTestUtil.getValidClientCreateInput();

        when(repository.save(Mockito.any(ClientJPAEntity.class))).thenThrow(
                new DataIntegrityViolationException("bla bla for key 'client.document_id_unique_constraint' bla bla")
        );

        Assertions.assertThatThrownBy(() -> clientCreateUseCase.create(validInput))
                .isInstanceOf(ClientUniqueDocumentException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.DUPLICATED_RECORD);
    }

    @Test
    public void Should_ThrowUniqueEmailException_WhenDocumentIsDuplicated() {
        var validInput = ClientTestUtil.getValidClientCreateInput();

        when(repository.save(Mockito.any(ClientJPAEntity.class))).thenThrow(
                new DataIntegrityViolationException("bla bla for key 'client.email_unique_constraint' bla bla")
        );

        Assertions.assertThatThrownBy(() -> clientCreateUseCase.create(validInput))
                .isInstanceOf(ClientUniqueEmailException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.DUPLICATED_RECORD);
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
    public void Should_ThrowInvalidInputException_WhenPhoneIsInvalid(String input) {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().phone(input).build(),
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
