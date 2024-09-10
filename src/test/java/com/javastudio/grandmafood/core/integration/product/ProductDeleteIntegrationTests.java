package com.javastudio.grandmafood.core.integration.product;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.core.utils.ProductTestUtil;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.usecases.product.ProductCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductDeleteUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductFindUseCase;
import com.javastudio.grandmafood.features.errors.ProductNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductDeleteIntegrationTests {

    @Autowired
    private ProductDeleteUseCase productDeleteUseCase;

    @Autowired
    private ProductCreateUseCase productCreateUseCase;

    @Autowired
    private ProductFindUseCase productFindUseCase;

    private Product product;

    @BeforeEach
    public void setUp() {
        var validInput1 = ProductTestUtil.getValidProductCreateInput();
        this.product = productCreateUseCase.create(validInput1);

        var validInput2 = ProductTestUtil.getValidProductCreateInput().toBuilder()
                .name("CHEESE BURGER BIG COMBO")
                .build();
        productCreateUseCase.create(validInput2);
    }

    @Test
    public void Should_DeleteProduct_GivenProductId() {

        productDeleteUseCase.deleteById(product.getUuid());

        var productFound = productFindUseCase.findById(product.getUuid());

        Assertions.assertThat(productFound).isEmpty();
    }

    @Test
    public void Should_ThrowNotFoundException_WhenProductIsNotFound() {

        Assertions.assertThatThrownBy(() -> productDeleteUseCase.deleteById(UUID.randomUUID()))
                .isInstanceOf(ProductNotFoundException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }

}
