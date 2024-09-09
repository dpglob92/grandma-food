package com.javastudio.grandmafood.features.core.controllers.order.dto;

import com.javastudio.grandmafood.common.DateTimeUtils;
import com.javastudio.grandmafood.common.exceptions.ValidationUtils;
import com.javastudio.grandmafood.features.core.controllers.client.ClientDocumentUtils;
import com.javastudio.grandmafood.features.core.entities.orders.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class OrderDTOMapper {

    public OrderDTO domainToDTO(Order order, OrderInfo orderInfo) {
        String document = ClientDocumentUtils.concatenateDocument(
                order.getClient().getDocumentType(),
                order.getClient().getDocumentId()
        );
        // based on the validations we will always have at least one element
        OrderItem firstOrderItem = order.getOrderItems().getFirst();

        UUID productUuid = firstOrderItem.getProduct().getUuid();
        int quantity = firstOrderItem.getQuantity();


        LocalDateTime deliveredAt = order.getDeliveredAt();
        if (deliveredAt != null){
            deliveredAt = DateTimeUtils.convertToBogotaTime(deliveredAt);
        }

        return OrderDTO.builder()
                .uuid(order.getUuid())
                .creationDateTime(DateTimeUtils.convertToBogotaTime(order.getCreatedAt()))
                .clientDocument(document)
                .productUuid(productUuid)
                .quantity(quantity)
                .additionalInformation(order.getAdditionalInformation())
                .subTotal(orderInfo.getSubTotal())
                .tax(orderInfo.getTax())
                .grandTotal(orderInfo.getGrandTotal())
                .deliveredAt(deliveredAt)
                .build();
    }

    public OrderCreateInput dtoToDomain(OrderCreateDTO createDTO) {
        ClientDocumentUtils.DocumentData documentData = ClientDocumentUtils.separateDocument(createDTO.getClientDocument());

        UUID productUuid = ValidationUtils.parseUUID(createDTO.getProductUuid());

        return OrderCreateInput.builder()
                .clientDocumentId(documentData.documentId())
                .additionalInformation(createDTO.getAdditionalInformation())
                .orderItems(List.of(
                        new OrderItemCreateInput(productUuid, createDTO.getQuantity())
                ))
                .build();
    }
}
