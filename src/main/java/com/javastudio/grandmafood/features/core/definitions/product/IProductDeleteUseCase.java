package com.javastudio.grandmafood.features.core.definitions.product;

import java.util.UUID;

public interface IProductDeleteUseCase {
    void deleteById(UUID uuid);
}
