package com.javastudio.grandmafood.core.integration.product;

import com.javastudio.grandmafood.core.utils.ProductTestUtil;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.usecases.product.ProductCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductFindUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductFindIntegrationTests {

    @Autowired
    private ProductCreateUseCase createUseCase;

    @Autowired
    private ProductFindUseCase findUseCase;

    private Product product1;

    @BeforeEach
    public void setup() {
        var validInput1 = ProductTestUtil.getValidProductCreateInput();
        this.product1 = createUseCase.create(validInput1);
    }

    @Test
    public void Should_FindProduct_GivenId() {

        var productFound = findUseCase.findById(product1.getUuid());

        Assertions.assertThat(productFound).isNotEmpty();
        Assertions.assertThat(productFound.get().getUuid()).isEqualTo(product1.getUuid());
    }

    @Test
    public void Should_NotFindProduct_GivenId() {

        var productFound = findUseCase.findById(UUID.randomUUID());

        Assertions.assertThat(productFound).isEmpty();
    }
}
