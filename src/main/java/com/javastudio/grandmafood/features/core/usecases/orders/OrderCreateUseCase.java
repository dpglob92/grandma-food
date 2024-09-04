package com.javastudio.grandmafood.features.core.usecases.orders;

import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.database.adapters.ClientAdapter;
import com.javastudio.grandmafood.features.core.database.adapters.ProductAdapter;
import com.javastudio.grandmafood.features.core.database.entities.ClientJPAEntity;
import com.javastudio.grandmafood.features.core.database.entities.OrderItemJPAEntity;
import com.javastudio.grandmafood.features.core.database.entities.OrderJPAEntity;
import com.javastudio.grandmafood.features.core.database.repositories.OrderItemJPAEntityRepository;
import com.javastudio.grandmafood.features.core.database.repositories.OrderJPAEntityRepository;
import com.javastudio.grandmafood.features.core.definitions.orders.IOrderCreateUseCase;
import com.javastudio.grandmafood.features.core.entities.client.Client;
import com.javastudio.grandmafood.features.core.entities.orders.Order;
import com.javastudio.grandmafood.features.core.entities.orders.OrderCreateInput;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItem;
import com.javastudio.grandmafood.features.core.entities.orders.OrderItemCreateInput;
import com.javastudio.grandmafood.features.core.entities.product.Product;
import com.javastudio.grandmafood.features.core.usecases.client.ClientFindUseCase;
import com.javastudio.grandmafood.features.core.usecases.product.ProductFindUseCase;
import com.javastudio.grandmafood.features.errors.ClientNotFoundException;
import com.javastudio.grandmafood.features.errors.ProductNotFoundException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderCreateUseCase implements IOrderCreateUseCase {

    Logger logger = LoggerFactory.getLogger(OrderCreateUseCase.class);

    private final ProductFindUseCase productFindUseCase;

    private final ClientFindUseCase clientFindUseCase;

    private final OrderItemJPAEntityRepository orderItemJPAEntityRepository;

    private final OrderJPAEntityRepository orderJPAEntityRepository;

    private final ClientAdapter clientAdapter;

    private final ProductAdapter productAdapter;

    private final Validator validator;

    public OrderCreateUseCase(
            ProductFindUseCase productFindUseCase,
            ClientFindUseCase clientFindUseCase,
            OrderJPAEntityRepository orderJPAEntityRepository,
            OrderItemJPAEntityRepository orderItemJPAEntityRepository,
            ClientAdapter clientAdapter,
            ProductAdapter productAdapter,
            Validator validator
    ) {
        this.productFindUseCase = productFindUseCase;
        this.clientFindUseCase = clientFindUseCase;
        this.orderJPAEntityRepository = orderJPAEntityRepository;
        this.orderItemJPAEntityRepository = orderItemJPAEntityRepository;
        this.clientAdapter = clientAdapter;
        this.productAdapter = productAdapter;
        this.validator = validator;
    }

    @Override
    public Order create(OrderCreateInput input) {
        logger.info("Validating order create input");
        ValidationUtils.validate(validator, input);

        logger.info("Finding client with document id of {}", input.getClientDocumentId());
        Optional<Client> clientContainer = clientFindUseCase.findByDocument(input.getClientDocumentId());

        if (clientContainer.isEmpty()) {
            throw new ClientNotFoundException();
        }

        ClientJPAEntity clientJPAEntity = clientAdapter.domainToJPAEntity(clientContainer.get());

        // TODO: improve performance by selecting in batch

        logger.info("Finding all products for this order");
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemCreateInput o : input.getOrderItems()) {
            Optional<Product> pContainer = productFindUseCase.findById(o.getProductId());
            if (pContainer.isEmpty()) {
                throw new ProductNotFoundException(o.getProductId().toString());
            }
            orderItems.add(new OrderItem(pContainer.get(), o.getQuantity()));
        }

        logger.info("Saving order entity with a total of {} order items", orderItems.size());
        OrderJPAEntity orderJPAEntity = orderJPAEntityRepository.save(
                OrderJPAEntity.builder()
                        .client(clientJPAEntity)
                        .additionalInformation(input.getAdditionalInformation())
                        .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                        .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                        .build()
        );

        List<OrderItemJPAEntity> orderItemJPAEntitiesToSave = orderItems.stream()
                .map(orderItem -> OrderItemJPAEntity.builder()
                        .order(orderJPAEntity)
                        .product(productAdapter.domainToJPAEntity(orderItem.getProduct()))
                        .quantity(orderItem.getQuantity())
                        .build()
                ).toList();

        logger.info("Saving order items entity with a total of {} order items", orderItems.size());
        orderItemJPAEntityRepository.saveAll(orderItemJPAEntitiesToSave);

        return Order.builder()
                .uuid(orderJPAEntity.getId())
                .client(clientContainer.get())
                .orderItems(orderItems)
                .additionalInformation(orderJPAEntity.getAdditionalInformation())
                .deliveredAt(orderJPAEntity.getDeliveredAt())
                .createdAt(orderJPAEntity.getCreatedAt())
                .updatedAt(orderJPAEntity.getUpdatedAt())
                .build();
    }

}
