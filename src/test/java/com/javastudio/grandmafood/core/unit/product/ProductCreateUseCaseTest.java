package com.javastudio.grandmafood.core.unit.product;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.core.utils.ProductTestUtil;
import com.javastudio.grandmafood.features.core.database.adapters.ProductAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ProductJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.ProductJPAEntityRepository;
import com.javastudio.grandmafood.features.core.entities.product.ProductCreateInput;
import com.javastudio.grandmafood.features.core.usecases.product.ProductCreateUseCase;
import com.javastudio.grandmafood.features.errors.ProductUniqueNameException;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductCreateUseCaseTest {

    @Mock
    private ProductJPAEntityRepository repository;

    private ProductCreateUseCase createUseCase;

    @BeforeEach
    public void setup() {
        createUseCase = new ProductCreateUseCase(repository, Validation.buildDefaultValidatorFactory().getValidator());
    }

    private void commonInvalidInputTest(Function<ProductCreateInput, ProductCreateInput> fieldSetter, String fieldName) {
        var validInput = ProductTestUtil.getValidProductCreateInput();
        var finalInput = fieldSetter.apply(validInput);
        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> createUseCase.create(finalInput),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.getFirst().path()).isEqualTo(fieldName);
    }

    @Test
    public void Should_CreateProductWithNameInUpperCase_WhenDataIsValid() {
        var validInput = ProductTestUtil.getValidProductCreateInput();
        var productJPAEntity = ProductAdapter.domainToJPAEntity(ProductTestUtil.getValidProduct());

        createUseCase.create(validInput);

        verify(repository).save(Mockito.eq(productJPAEntity));
    }

    @Test
    public void Should_ThrowUniqueNameException_WhenNameIsDuplicated() {
        var validInput = ProductTestUtil.getValidProductCreateInput();

        when(repository.save(Mockito.any(ProductJPAEntity.class))).thenThrow(
                new DataIntegrityViolationException("bla bla for key 'product.name_id_unique_constraint' bla bla")
        );

        Assertions.assertThatThrownBy(() -> createUseCase.create(validInput))
                .isInstanceOf(ProductUniqueNameException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.DUPLICATED_RECORD);
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
    public void Should_ThrowInvalidInputException_WhenDescriptionIsTooLong() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().description("a".repeat(512)).build(),
                "description"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenFoodCategoryIsNull() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().foodCategory(null).build(),
                "foodCategory"
        );
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenPriceIsNegative() {
        commonInvalidInputTest(
                createInput -> createInput.toBuilder().price(BigDecimal.valueOf(-10.99)).build(),
                "price"
        );
    }
}
