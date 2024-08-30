package com.javastudio.grandmafood.core.unit.product;

import com.javastudio.grandmafood.common.exceptions.ExceptionCode;
import com.javastudio.grandmafood.features.core.database.repositories.ProductJPAEntityRepository;
import com.javastudio.grandmafood.features.core.usecases.product.ProductDeleteUseCase;
import com.javastudio.grandmafood.features.errors.ProductNotFoundException;
import jakarta.validation.Validation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductDeleteUseCaseTest {
    @Mock
    private ProductJPAEntityRepository repository;

    private ProductDeleteUseCase deleteUseCase;

    @BeforeEach
    public void setUp() {
        deleteUseCase = new ProductDeleteUseCase(repository, Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Test
    public void Should_ThrowNotFoundException_WhenProductIsNotFound() {
        UUID validUuid = UUID.fromString("9b9b2c15-b3df-4724-86e2-04c65af0eecd");
        when(repository.findById(validUuid)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> deleteUseCase.deleteById(validUuid))
                .isInstanceOf(ProductNotFoundException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND);
    }
}
