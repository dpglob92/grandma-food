package com.javastudio.grandmafood.core.unit.product;

import com.javastudio.grandmafood.common.exceptions.FieldError;
import com.javastudio.grandmafood.common.exceptions.InvalidInputException;
import com.javastudio.grandmafood.features.core.controllers.product.ProductDTOMapper;
import com.javastudio.grandmafood.features.core.controllers.product.dto.ProductCreateDTO;
import com.javastudio.grandmafood.features.core.controllers.product.dto.ProductUpdateDTO;
import com.javastudio.grandmafood.features.core.entities.product.FoodCategory;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
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
    public void Should_MapUpdateInputData_WhenDataIsValid() {
        var data = getValidUpdateDTO();
        var input = productDTOMapper.updateDTOToDomain(data);

        // we are only interested in those fields who contain some business logic
        Assertions.assertThat(input.getPrice()).isEqualTo(new BigDecimal("250.32"));
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenCreateDTOPriceIsNull() {
        var data = getValidCreateDTO();
        data.setPrice(null);

        assertInvalidPriceInput(() -> productDTOMapper.dtoToDomain(data));
    }

    @Test
    public void Should_ThrowInvalidInputException_WhenUpdateDTOPriceIsNull() {
        var data = getValidUpdateDTO();
        data.setPrice(null);

        assertInvalidPriceInput(() -> productDTOMapper.updateDTOToDomain(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "14o1", "145,90", "--10"})
    public void Should_ThrowInvalidInputException_WhenCreateDTOPriceIsInvalid(String input) {
        var data = getValidCreateDTO();
        data.setPrice(input);

        assertInvalidPriceInput(() -> productDTOMapper.dtoToDomain(data));
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "14o1", "145,90", "--10"})
    public void Should_ThrowInvalidInputException_WhenUpdateDTOPriceIsInvalid(String input) {
        var data = getValidUpdateDTO();
        data.setPrice(input);

        assertInvalidPriceInput(() -> productDTOMapper.updateDTOToDomain(data));
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

    private ProductUpdateDTO getValidUpdateDTO() {
        return new ProductUpdateDTO(
                "Juan",
                "ipsum pot",
                FoodCategory.CHICKEN,
                "250.32",
                false
        );
    }

    private void assertInvalidPriceInput(ThrowableAssert.ThrowingCallable callMapperMethod){
        InvalidInputException throwable = Assertions.catchThrowableOfType(
                callMapperMethod,
                InvalidInputException.class
        );
        Assertions.assertThat(throwable).withFailMessage("InvalidInputException was not thrown").isNotNull();
        List<FieldError> errors = throwable.getErrors();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.getFirst().path()).isEqualTo("price");
    }

}
