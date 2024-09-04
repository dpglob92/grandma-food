package com.javastudio.grandmafood.features.core.database.projections;

import com.javastudio.grandmafood.features.core.database.entities.ProductJPAEntity;

public interface OrderItemView {

    ProductJPAEntity getProduct();

    Integer getQuantity();
}
