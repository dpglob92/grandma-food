package com.javastudio.grandmafood.core.unit.product;

import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.features.core.controllers.product.ProductDTOMapper;
import com.javastudio.grandmafood.features.core.controllers.product.dto.ProductCreateDTO;
import com.javastudio.grandmafood.features.core.entities.product.FoodCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

public class ProductDTOMapperTest {

    private final ProductDTOMapper productDTOMapper;

    public ProductDTOMapperTest() {
        this.productDTOMapper = new ProductDTOMapper();
    }

    @Test
    public void Should_MapCreateInputData_WhenDataIsValid() {
        var data = getValidCreateDTO();
        var input = productDTOMapper.dtoToDomain(data);

        // we are only interested in those fields who contain some business logic
        Assertions.assertThat(input.getPrice()).isEqualTo(new BigDecimal("100.32"));
    }

    @Test
    public void Should_Should_ThrowInvalidInputException_WhenPriceIsNull() {
        var data = getValidCreateDTO();
        data.setPrice(null);

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> productDTOMapper.dtoToDomain(data),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.getFirst().path()).isEqualTo("price");
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "14o1", "145,90", "--10"})
    public void Should_Should_ThrowInvalidInputException_WhenPriceIsInvalid(String input) {
        var data = getValidCreateDTO();
        data.setPrice(input);

        InvalidInputException throwable = Assertions.catchThrowableOfType(
                () -> productDTOMapper.dtoToDomain(data),
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.getFirst().path()).isEqualTo("price");
    }

    private ProductCreateDTO getValidCreateDTO() {
        return new ProductCreateDTO(
                "Pepe",
                "lorem ipsum",
                FoodCategory.CHICKEN,
                "100.32",
                true
        );
    }
}
