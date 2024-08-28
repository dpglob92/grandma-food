package com.javastudio.grandmafood.core.integration.product;

import com.javastudio.grandmafood.core.utils.CustomAssertions;
import com.javastudio.grandmafood.core.utils.ProductTestUtil;
import com.javastudio.grandmafood.features.core.usecases.product.ProductCreateUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductFindUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductCreateIntegrationTests {

    @Autowired
    private ProductCreateUseCase createUseCase;

    @Autowired
    private ProductFindUseCase findUseCase;

    @Test
    public void Should_CreateAndPersistProduct_WhenDataIsValid() {
        var validInput = ProductTestUtil.getValidProductCreateInput();
        var createdProduct = createUseCase.create(validInput);

        var retrievedProduct = findUseCase.findById(createdProduct.getUuid());

        if (retrievedProduct.isPresent()) {
            var currentProduct = retrievedProduct.get();
            Assertions.assertThat(currentProduct.getName()).isEqualTo(validInput.getName());
            Assertions.assertThat(currentProduct.getDescription()).isEqualTo(validInput.getDescription());
            Assertions.assertThat(currentProduct.getFoodCategory()).isEqualTo(validInput.getFoodCategory());
            Assertions.assertThat(currentProduct.getPrice()).isEqualTo(validInput.getPrice());
            Assertions.assertThat(currentProduct.isAvailable()).isEqualTo(validInput.isAvailable());

            CustomAssertions.assertDateTimeIsCloseToNow(currentProduct.getCreatedAt(), 5);
            CustomAssertions.assertDateTimeIsCloseToNow(currentProduct.getUpdatedAt(), 5);
            Assertions.assertThat(currentProduct.getDeletedAt()).isEqualTo(null);
        } else {
            Assertions.fail("product not found");
        }

    }
}
