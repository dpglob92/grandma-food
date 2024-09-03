package com.javastudio.grandmafood.features.core.usecases.orders;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.definitions.orders.IOrderCreateUseCase;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductFindUseCase;
import jakarta.validation.Validator;

public class OrderCreateUseCase implements IOrderCreateUseCase {

    private final ProductFindUseCase productFindUseCase;

    private final ClientFindUseCase clientFindUseCase;

    private final Validator validator;

    public OrderCreateUseCase(
            ProductFindUseCase productFindUseCase,
            ClientFindUseCase clientFindUseCase,
            Validator validator
    ) {
        this.productFindUseCase = productFindUseCase;
        this.clientFindUseCase = clientFindUseCase;
        this.validator = validator;
    }

    @Override
    public Order create(OrderCreateInput input) {
        ValidationUtils.validate(validator, input);

        return null;
    }
}
